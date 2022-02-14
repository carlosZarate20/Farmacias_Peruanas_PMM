package com.farmaciasperuanas.pmmli.materialinformation.service;

import com.farmaciasperuanas.pmmli.materialinformation.dto.*;
import com.farmaciasperuanas.pmmli.materialinformation.entity.Material;
import com.farmaciasperuanas.pmmli.materialinformation.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.materialinformation.repository.MaterialRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
public class MaterialServiceImpl implements MaterialService{
    private static final Logger LOGGER = LogManager.getLogger(BarcodeServiceImpl.class);

    private static final Gson GSON = new Gson();

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private TransactionLogService transactionLogService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private TransactionLogErrorService transactionLogErrorService;

    @Override
    public ResponseDto enviarMaterial() {

        List<MaterialDto> materialDtoList = new ArrayList<>();
        ResponseDto responseDto = new ResponseDto();
        ResponseApi responseApi = new ResponseApi();

        String urlString = "https://dev-logisticainversa.solucionesfps.pe/master_table/load_master_material";

        String responseBody = "";
        String requestBody = "";

        String authTokenHeader = "";
        LoginRequest loginRequest = new LoginRequest();

        String status = "";

        try{
            materialRepository.procedureUpdatedMaterial();

            materialDtoList = getListMaterial();

            if(materialDtoList.size() != 0){

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
//                String input = GSON.toJson(materialDtoList);
                String input = "";
                try {
                    input = mapper.writeValueAsString(materialDtoList);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                OutputStream os = httpUrlConnection.getOutputStream();
                os.write(input.getBytes());
                os.flush();

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
                    status = materialDtoList.size() == responseApi.getErrors().size() ? "F" : "FP";
                }

                TransactionLog tl = transactionLogService.saveTransactionLog("Maestro Material", "M",
                        "MM", "Data Maestra",
                        status, input, responseBody, null);
                for(ResponseApiErrorItem res : responseApi.getErrors()){
                    transactionLogErrorService.saveTransactionLogError(tl,res.getPk(),res.getMessage());
                }
                List<Integer> positionsError = responseApi.getErrors().stream()
                        .map(ResponseApiErrorItem::getPosition)
                        .collect(Collectors.toList());
                for (MaterialDto materialDto : materialDtoList) {
                    boolean valid = positionsError.contains(materialDtoList.indexOf(materialDto));
                    if(!valid) {
                        materialRepository.updateMaterial(materialDto.getInka());
                    }

                }
                responseApi.setId(tl.getIdTransacctionLog());

                if(responseApi.getCode().equalsIgnoreCase("ok")){

//                    for(MaterialDto materialDto: materialDtoList){
//                        materialRepository.updateMaterial(materialDto.getInka());
//                    }
                    status = "C";
                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(true);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Registro Correcto");
                } else {
                    status = materialDtoList.size() == responseApi.getErrors().size() ? "F" : "FP";
                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(false);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Ocurrio un error");
                }

//                responseBody = String.valueOf(responseDto);
//                requestBody = GSON.toJson(materialDtoList);

            } else {
                responseDto.setCode(HttpStatus.OK.value());
                responseDto.setStatus(false);
                responseDto.setBody(responseApi);
                responseDto.setMessage("No existen registros en la tabla SWLI.MATERIAL");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return responseDto;
    }
    private List<MaterialDto> getListMaterial(){
        List<MaterialDto> materialDtoList = new ArrayList<>();
        List<Material> materialList = new ArrayList<>();
        materialList = materialRepository.getListMaterial();

        for(Material material: materialList){
            MaterialDto materialDto = new MaterialDto();
            materialDto.setControl(material.getControl());
            materialDto.setDescripcion(material.getDescription());
            materialDto.setFamilia(material.getFamily());
            materialDto.setInka(material.getInka());
            materialDto.setMifa(material.getMifa());
            materialDto.setPrecioUnitario(material.getUnitPrice());
            materialDto.setSurtido(material.getAssortment());
            materialDto.setZonaInduccion(material.getInductionZone());

            materialDtoList.add(materialDto);
        }
        return materialDtoList;
    }
}
