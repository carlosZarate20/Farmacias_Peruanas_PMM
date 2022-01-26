package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.service;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto.ProviderDto;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto.ResponseApi;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto.ResponseDto;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.entity.Provider;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.entity.TransactionLog;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.repository.ProviderRepository;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.repository.TransactionLogRepository;
import com.google.gson.Gson;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProviderServiceImpl implements ProviderService {

    private static final Logger LOGGER = LogManager.getLogger(LoginServiceImpl.class);

    private static final Gson GSON = new Gson();

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private TransactionLogService transactionLogService;

    @Override
    public ResponseDto registrarProveedorLi(HttpServletRequest httpSession) {

        ResponseDto responseDto = new ResponseDto();
        String urlString = "https://dev-logisticainversa.solucionesfps.pe/master_table/load_master_provider";

        List<ProviderDto> providerDtoList = new ArrayList<>();
        ResponseApi responseApi = new ResponseApi();
        String responseBody = "";
        String requestBody = "";

        try {
            //Traer datos para insertar a LI
            providerDtoList = getListProvider();

            if(providerDtoList.size() != 0){
                String authTokenHeader  = httpSession.getHeader("Authorization");

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
            /*httpUrlConnection.setRequestProperty("applicationCode", applicationCode);
            httpUrlConnection.setRequestProperty("transactionId", transactionId);*/
                httpUrlConnection.setRequestMethod("POST");

                String input = GSON.toJson(providerDtoList);

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

                if(responseApi.getCode().equalsIgnoreCase("ok")){

                    for(ProviderDto providerDto: providerDtoList)
                    {
                        providerRepository.updateProvider(providerDto.getCodigoSap());
                    }

                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(true);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Registro Correcto");
                } else {
                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(false);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Ocurrio un error");
                }

                responseBody = String.valueOf(responseDto);
                requestBody = GSON.toJson(providerDtoList);

                transactionLogService.saveTransactionLog("Maestro Provider", "M",
                        "MP", "Data Maestra",
                        true, requestBody, responseBody);

            } else{
                responseDto.setCode(HttpStatus.OK.value());
                responseDto.setStatus(false);
                responseDto.setBody(responseApi);
                responseDto.setMessage("No existen registros en la tabla SWLI.PROVIDER");
            }

        }catch (Exception e){
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
