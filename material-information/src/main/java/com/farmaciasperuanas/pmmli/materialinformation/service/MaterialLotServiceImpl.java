package com.farmaciasperuanas.pmmli.materialinformation.service;

import com.farmaciasperuanas.pmmli.materialinformation.dto.*;
import com.farmaciasperuanas.pmmli.materialinformation.entity.MaterialLot;
import com.farmaciasperuanas.pmmli.materialinformation.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.materialinformation.repository.MaterialLotRepository;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialLotServiceImpl implements MaterialLotService{

    private static final Logger LOGGER = LogManager.getLogger(BarcodeServiceImpl.class);

    private static final Gson GSON = new Gson();

    @Autowired
    private MaterialLotRepository materialLotRepository;

    @Autowired
    private TransactionLogService transactionLogService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private TransactionLogErrorService transactionLogErrorService;

    @Override
    public ResponseDto enviarMaterialLot() {

        ResponseDto responseDto = new ResponseDto();
        ResponseApi responseApi = new ResponseApi();
        List<MaterialLotDto> materialLotDtoList = new ArrayList<>();

        String urlString = "https://dev-logisticainversa.solucionesfps.pe/master_table/load_master_material_lot";

        String responseBody = "";
        String requestBody = "";

        String authTokenHeader = "";
        LoginRequest loginRequest = new LoginRequest();

        String status = "";
        try{
            materialLotDtoList = getListMaterialLot();

            if (materialLotDtoList.size() != 0) {

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
//                String input = GSON.toJson(materialLotDtoList);
                String input = "";
                try {
                    input = mapper.writeValueAsString(materialLotDtoList);
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
                    status = materialLotDtoList.size() == responseApi.getErrors().size() ? "F" : "FP";
                }


                TransactionLog tl = transactionLogService.saveTransactionLog("Maestro Material Lot", "M",
                        "MML", "Data Maestra",
                        status, input, responseBody, null);

                for(ResponseApiErrorItem res : responseApi.getErrors()){
                    transactionLogErrorService.saveTransactionLogError(tl,res.getPk(),res.getMessage());
                }

                List<Integer> positionsError = responseApi.getErrors().stream()
                        .map(ResponseApiErrorItem::getPosition)
                        .collect(Collectors.toList());
                for (MaterialLotDto materialLotDto : materialLotDtoList) {
                    boolean valid = positionsError.contains(materialLotDtoList.indexOf(materialLotDto));
                    if(!valid) {
                        materialLotRepository.updateMaterialLot(materialLotDto.getMaterial(),materialLotDto.getLote());
                    }
                }

                responseApi.setId(tl.getIdTransacctionLog());

                if(responseApi.getCode().equalsIgnoreCase("ok")){

//                    for(MaterialLotDto materialLotDto: materialLotDtoList){
//                        materialLotRepository.updateMaterialLot(materialLotDto.getMaterial());
//                    }
                    status = "C";
                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(true);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Registro Correcto");

                } else {
                    status = materialLotDtoList.size() == responseApi.getErrors().size() ? "F" : "FP";
                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(false);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Ocurrio un error");
                }
//                responseBody = String.valueOf(responseApi);
//                requestBody = GSON.toJson(materialLotDtoList);

            } else {
                responseDto.setCode(HttpStatus.OK.value());
                responseDto.setStatus(false);
                responseDto.setBody(responseApi);
                responseDto.setMessage("No existen registros en la tabla SWLI.MATERIAL_LOT");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return responseDto;
    }

    private List<MaterialLotDto> getListMaterialLot(){
        List<MaterialLotDto> materialLotDtoList = new ArrayList<>();
        List<MaterialLot> materialLotList = new ArrayList<>();

        materialLotList = materialLotRepository.getListMaterialLot();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        for(MaterialLot materialLot: materialLotList){
            MaterialLotDto materialLotDto = new MaterialLotDto();
            materialLotDto.setFechaCaducidad(dateFormat.format(materialLot.getExpireDate()));
            materialLotDto.setLote(materialLot.getLote());
            materialLotDto.setMaterial(materialLot.getMaterialCode());

            materialLotDtoList.add(materialLotDto);
        }

        return materialLotDtoList;
    }
}
