package com.farmaciasperuanas.pmmli.provider.service;

import com.farmaciasperuanas.pmmli.provider.dto.*;
import com.farmaciasperuanas.pmmli.provider.entity.MaterialProvider;
import com.farmaciasperuanas.pmmli.provider.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.provider.repository.MaterialProviderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialProviderServiceImpl implements MaterialProviderService{

    private static final Logger LOGGER = LogManager.getLogger(LoginServiceImpl.class);

    private static final Gson GSON = new Gson();

    @Autowired
    private MaterialProviderRepository materialProviderRepository;

    @Autowired
    private TransactionLogService transactionLogService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private TransactionLogErrorService transactionLogErrorService;

    @Override
    public ResponseDto enviarMaterialProvider() {

        List<MaterialProviderDto> materialProviderDtoList = new ArrayList<>();
        ResponseDto responseDto = new ResponseDto();
        String urlString = "https://dev-logisticainversa.solucionesfps.pe/master_table/load_master_material_provider";
        ResponseApi responseApi = new ResponseApi();

        String responseBody = "";
        String requestBody = "";

        String authTokenHeader = "";
        LoginRequest loginRequest = new LoginRequest();

        String status = "";
        try{
            materialProviderRepository.procedureUpdateMaterialProvider();

            materialProviderDtoList = getListMaterialProvider();

            if(materialProviderDtoList.size() != 0){
                loginRequest.setUsername("serviciosweb");
                loginRequest.setPassword("Brainbox2021");
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
//                String input = GSON.toJson(materialProviderDtoList);
                String input = "";
                try {
                    input = mapper.writeValueAsString(materialProviderDtoList);
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
                    status = materialProviderDtoList.size() == responseApi.getErrors().size() ? "F" : "FP";
                }

                TransactionLog tl = transactionLogService.saveTransactionLog("Maestro Material Provider", "M",
                        "MMP", "Data Maestra",
                        status, input, responseBody, null);

                for(ResponseApiErrorItem res : responseApi.getErrors()){
                    transactionLogErrorService.saveTransactionLogError(tl,res.getPk(),res.getMessage());
                }

                List<Integer> positionsError = responseApi.getErrors().stream()
                        .map(ResponseApiErrorItem::getPosition)
                        .collect(Collectors.toList());

                for(MaterialProviderDto materialProviderDto : materialProviderDtoList){
                    boolean valid = positionsError.contains(materialProviderDtoList.indexOf(materialProviderDto));
                    if(!valid) {
                        materialProviderRepository.updateMaterialProvider(materialProviderDto.getMaterialInka(),materialProviderDto.getCodigoProveedor());
                    }
                }
                responseApi.setId(tl.getIdTransacctionLog());

                if(responseApi.getCode().equalsIgnoreCase("ok")){
//                    for(MaterialProviderDto materialProviderDto: materialProviderDtoList){
//                        materialProviderRepository.updateMaterialProvider(materialProviderDto.getMaterialInka());
//                    }
                    status = "C";
                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(true);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Registro Correcto");
                } else {
                    status = materialProviderDtoList.size() == responseApi.getErrors().size() ? "F" : "FP";
                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(false);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Ocurrio un error");
                }

//                responseBody = String.valueOf(responseApi);
//                requestBody = GSON.toJson(materialProviderDtoList);


            } else {
                responseDto.setCode(HttpStatus.OK.value());
                responseDto.setStatus(false);
                responseDto.setBody(responseApi);
                responseDto.setMessage("No existen registros en la tabla SWLI.MATERIAL_PROVIDER");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return responseDto;
    }

    private List<MaterialProviderDto> getListMaterialProvider(){
        List<MaterialProviderDto> materialProviderDtoList = new ArrayList<>();
        List<MaterialProvider> materialProviderList = new ArrayList<MaterialProvider>();

        materialProviderList = materialProviderRepository.geListMaterialProvider();

        for(MaterialProvider materialProvider: materialProviderList){
            MaterialProviderDto materialProviderDto = new MaterialProviderDto();
            materialProviderDto.setCodigoLaboratorio(materialProvider.getLabCode());
            materialProviderDto.setCodigoProveedor(materialProvider.getProviderCode());
            materialProviderDto.setMaterialInka(materialProvider.getMaterialInka());
            materialProviderDto.setMaterialMifa(materialProvider.getMaterialMifa());
            materialProviderDto.setProveedorPrincipal(materialProvider.getPrincipalProvider());

            materialProviderDtoList.add(materialProviderDto);
        }

        return materialProviderDtoList;
    }
}
