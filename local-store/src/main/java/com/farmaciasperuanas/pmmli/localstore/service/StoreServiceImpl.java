package com.farmaciasperuanas.pmmli.localstore.service;

import com.farmaciasperuanas.pmmli.localstore.dto.*;
import com.farmaciasperuanas.pmmli.localstore.entity.Store;
import com.farmaciasperuanas.pmmli.localstore.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.localstore.entity.TransactionLogError;
import com.farmaciasperuanas.pmmli.localstore.repository.StoreRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreServiceImpl implements StoreService{

    private static final Logger LOGGER = LogManager.getLogger(StoreServiceImpl.class);

    private static final Gson GSON = new Gson();

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private TransactionLogService transactionLogService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private TransactionLogErrorService transactionLogErrorService;

    @Override
    public ResponseDto enviarTienda() {

        List<StoreDto> storeDtoList = new ArrayList<>();

        ResponseDto responseDto = new ResponseDto();
        ResponseApi responseApi = new ResponseApi();
        String urlString = "https://dev-logisticainversa.solucionesfps.pe/master_table/load_master_store";
        String responseBody = "";
        String requestBody = "";

        String authTokenHeader = "";
        LoginRequest loginRequest = new LoginRequest();

        String status = "";
        try{
            storeDtoList = getListStore();

            if(storeDtoList.size() != 0){
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
                String input = "";
                try {
                    input = mapper.writeValueAsString(storeDtoList);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
//                String input = GSON.toJson(storeDtoList);

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

                if(responseApi.getCode().equalsIgnoreCase("ok")){
//                    for(StoreDto storeDto: storeDtoList){
//                        storeRepository.updateStore(storeDto.getCodigo());
//                    }
                    status = "C";
                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(true);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Registro Correcto");
                } else {
                    status = "F";
                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(false);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Ocurrio un error");
                }

//                responseBody = String.valueOf(responseApi);
//                String responseBody = "";
                try {
                    responseBody = mapper.writeValueAsString(responseApi);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                TransactionLog tl = transactionLogService.saveTransactionLog("Maestro Store", "M",
                        "MS", "Data Maestra",
                        status, input, responseBody);
                for(ResponseApiErrorItem res : responseApi.getErrors()){
                    transactionLogErrorService.saveTransactionLogError(tl,res.getPk(),res.getMessage());
                }

                List<Integer> positionsError = responseApi.getErrors().stream()
                        .map(ResponseApiErrorItem::getPosition)
                        .collect(Collectors.toList());
                for(StoreDto storeDto : storeDtoList){
                    boolean valid = positionsError.contains(storeDtoList.indexOf(storeDto));
                    if(!valid) {
                        storeRepository.updateStore(storeDto.getCodigo());
                    }
                }


            } else {
                responseDto.setCode(HttpStatus.OK.value());
                responseDto.setStatus(false);
                responseDto.setBody(responseApi);
                responseDto.setMessage("No existen registros en la tabla SWLI.STORE");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return responseDto;
    }

    private List<StoreDto> getListStore(){
        List<StoreDto> storeDtoList = new ArrayList<>();
        List<Store> storeList = new ArrayList<>();

        storeList = storeRepository.getListStore();

        for(Store store : storeList){
            StoreDto storeDto = new StoreDto();
            storeDto.setCalleNumero(store.getStreet());
            storeDto.setCodigo(store.getCode());
            storeDto.setCodigoFarma(store.getCodFarma());
            storeDto.setCorreo(store.getEmail());
            storeDto.setDepartamento(store.getDeparmetCode());
            storeDto.setDireccion(store.getAddress());
            storeDto.setDistrito(store.getDistrictCode());
            storeDto.setNombre(store.getName());
            storeDto.setProvincia(store.getProvinceCode());
            storeDto.setSociedad(store.getSociety());
            storeDto.setTipoLocal(store.getLocalType());

            storeDtoList.add(storeDto);
        }

        return storeDtoList;
    }
}
