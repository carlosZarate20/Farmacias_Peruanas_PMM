package com.farmaciasperuanas.pmmli.provider.service;

import com.farmaciasperuanas.pmmli.provider.dto.*;
import com.farmaciasperuanas.pmmli.provider.entity.Provider;
import com.farmaciasperuanas.pmmli.provider.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.provider.repository.ProviderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProviderServiceImpl implements ProviderService {

    private static final Logger LOGGER = LogManager.getLogger(LoginServiceImpl.class);

    private static final Gson GSON = new Gson();

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private TransactionLogService transactionLogService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private TransactionLogErrorService transactionLogErrorService;

    @Autowired
    private Environment env;

    @Override
    public ResponseDto registrarProveedorLi() {

        ResponseDto responseDto = new ResponseDto();
        String urlString = env.getProperty("application.url-provider");

        List<ProviderDto> providerDtoList = new ArrayList<>();
        ResponseApi responseApi = new ResponseApi();
        String responseBody = "";
        String requestBody = "";
        String authTokenHeader = "";
        LoginRequest loginRequest = new LoginRequest();
        String status = "";
        try {
            //Traer datos para insertar a LI
            providerDtoList = getListProvider();

            if(providerDtoList.size() != 0){
                loginRequest.setUsername(env.getProperty("application.username"));
                loginRequest.setPassword(env.getProperty("application.password"));
                authTokenHeader = loginService.iniciarSession(loginRequest);

                URL url = new URL(urlString);
                URLConnection urlConnection = url.openConnection();

                HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
                httpUrlConnection.setConnectTimeout(30_000);
                httpUrlConnection.setReadTimeout(30_000);
                httpUrlConnection.setDoInput(true);
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setRequestProperty("Content-Type", "application/json");
                httpUrlConnection.setRequestProperty("Accept", "application/json");
                httpUrlConnection.setRequestProperty("Authorization", authTokenHeader);
                httpUrlConnection.setRequestMethod("POST");
                ObjectMapper mapper = new ObjectMapper();
//                String input = GSON.toJson(providerDtoList);
                String input = "";
                try {
                    input = mapper.writeValueAsString(providerDtoList);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                OutputStream os = httpUrlConnection.getOutputStream();
                os.write(input.getBytes());
                os.flush();

                if(httpUrlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new RuntimeException("Failed : HTTP error code : " + httpUrlConnection.getResponseCode());
                } else {
                    LOGGER.debug("Success ----> " + httpUrlConnection.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(), StandardCharsets.UTF_8));
                String outPut;
                StringBuilder sb = new StringBuilder();

                while((outPut = br.readLine()) != null) {
                    LOGGER.debug(outPut);
                    sb.append(outPut);
                }
                responseApi = GSON.fromJson(sb.toString(), ResponseApi.class);
                httpUrlConnection.disconnect();

                try {
                    responseBody = mapper.writeValueAsString(responseApi);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                if (responseApi.getCode().equalsIgnoreCase("ok")) {
                    status = "C";
                } else {
                    status = providerDtoList.size() == responseApi.getErrors().size() ? "F" : "FP";
                }

                TransactionLog tl = transactionLogService.saveTransactionLog("Maestro Provider", "M",
                        "MP", "Data Maestra",
                        status, input, responseBody, null);
                for(ResponseApiErrorItem res : responseApi.getErrors()){
                    transactionLogErrorService.saveTransactionLogError(tl,res.getPk(),res.getMessage());
                }

                List<Integer> positionsError = responseApi.getErrors().stream()
                        .map(ResponseApiErrorItem::getPosition)
                        .collect(Collectors.toList());
                for(ProviderDto providerDto : providerDtoList){
                    boolean valid = positionsError.contains(providerDtoList.indexOf(providerDto));
                    if(!valid) {
                        providerRepository.updateProvider(providerDto.getCodigoSap());
                    }
                }

                responseApi.setId(tl.getIdTransacctionLog());
                if(responseApi.getCode().equalsIgnoreCase("ok")){

//                    for(ProviderDto providerDto: providerDtoList)
//                    {
//                        providerRepository.updateProvider(providerDto.getCodigoSap());
//                    }

                    status = "C";
                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(true);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Envio Correcto");
                } else {
                    status = providerDtoList.size() == responseApi.getErrors().size() ? "F" : "FP";
                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(false);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Ocurrio un error");
                }

//                responseBody = String.valueOf(responseApi);
//                requestBody = GSON.toJson(providerDtoList);


            } else{
                responseDto.setCode(HttpStatus.OK.value());
                responseDto.setStatus(false);
                responseDto.setBody(responseApi);
                responseDto.setMessage("No existen registros en la tabla SWLI.PROVIDER");
            }

        }catch (Exception e){
            responseDto.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatus(false);
            responseDto.setBody(e.getClass());
            responseDto.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return responseDto;
    }

    private List<ProviderDto> getListProvider(){
        List<ProviderDto> providerDtoList = new ArrayList<>();
        List<Provider> providerList = new ArrayList<>();
        providerList = providerRepository.geListProvider();

        for(Provider provider: providerList){
            ProviderDto providerDto = new ProviderDto();

            providerDto.setCodigoSap(provider.getCode());
            providerDto.setConcBusq(provider.getSearch());
            providerDto.setCorreo(provider.getEmail());
            providerDto.setDireccion(provider.getStreet());
            providerDto.setNif2(provider.getNif2());
            providerDto.setNombre(provider.getBusinessName());
            providerDto.setRamo(provider.getRamo());
            providerDto.setRegion(provider.getRegion());
            providerDto.setRuc(provider.getRuc());
            providerDto.setTelefono(provider.getPhone());

            providerDtoList.add(providerDto);
        }
        return providerDtoList;
    }
}
