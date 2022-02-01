package com.farmaciasperuanas.pmmli.materialinformation.service;

import com.farmaciasperuanas.pmmli.materialinformation.dto.*;
import com.farmaciasperuanas.pmmli.materialinformation.entity.Barcode;
import com.farmaciasperuanas.pmmli.materialinformation.repository.BarcodeRepository;
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

@Service
public class BarcodeServiceImpl implements BarcodeService{
    @Autowired
    private BarcodeRepository barcodeRepository;

    private static final Logger LOGGER = LogManager.getLogger(BarcodeServiceImpl.class);

    private static final Gson GSON = new Gson();

    @Autowired
    private TransactionLogService transactionLogService;

    @Autowired
    private LoginService loginService;

    @Override
    public ResponseDto enviarCodigoBarra() {
        BarcodeResponse barcodeResponse = new BarcodeResponse();
        ResponseDto responseDto = new ResponseDto();
        ResponseApi responseApi = new ResponseApi();
        ResponseApiError responseApiError = new ResponseApiError();
        ResponseApiForbidden responseApiForbidden = new ResponseApiForbidden();

        List<BarcodeDto> listBarcode = new ArrayList<>();

        String urlString = "https://dev-logisticainversa.solucionesfps.pe/master_table/load_master_barcode";

        String responseBody = "";
        String requestBody = "";

        String authTokenHeader = "";
        LoginRequest loginRequest = new LoginRequest();

        String status = "";
        try {
            listBarcode = getListBarcode();
            if(listBarcode.size() != 0){

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

                String input = GSON.toJson(listBarcode);

                OutputStream os = httpUrlConnection.getOutputStream();
                os.write(input.getBytes());
                os.flush();

            /*if(httpUrlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + httpUrlConnection.getResponseCode());
            } else {
                LOGGER.debug("Success ----> " + httpUrlConnection.getResponseCode());
            }*/

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

                    for(BarcodeDto barcodeDto: listBarcode){
                        barcodeRepository.updateBarcode(barcodeDto.getMaterial());
                    }
                    status = "C";
                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(true);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Registro Correcto");
                } else {
                    status =  "F";
                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(false);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Ocurrio error");
                }

                responseBody = String.valueOf(responseApi);
                requestBody = GSON.toJson(listBarcode);

                transactionLogService.saveTransactionLog("Maestro Barcode", "M",
                        "MB", "Data Maestra",
                        status, requestBody, responseBody);
            } else {
                responseDto.setCode(HttpStatus.OK.value());
                responseDto.setStatus(false);
                responseDto.setBody(responseApi);
                responseDto.setMessage("No existen registros en la tabla SWLI.BARCODE");
            }


        } catch (Exception e){
            responseApiForbidden.setMessage(e.getMessage());
            responseApiForbidden.setStatus(HttpStatus.FORBIDDEN.value());
            responseDto.setBody(responseApiForbidden);
            e.printStackTrace();
        }
        return responseDto;
    }

    private List<BarcodeDto> getListBarcode(){
        List<BarcodeDto> listBarcode = new ArrayList<>();
        List<Barcode> listAllBarcode = new ArrayList<>();
        listAllBarcode = barcodeRepository.getListBarcode();
        /*List<Barcode> listAllBarcode = StreamSupport.stream(this.barcodeRepository.getListBarcode().spliterator(),false)
                .collect(Collectors.toList());*/

        for(Barcode barcode: listAllBarcode){
            BarcodeDto barcodeRequest = new BarcodeDto();
            barcodeRequest.setEanUPC(barcode.getEanUPC());
            barcodeRequest.setJEan(barcode.getJEan());
            barcodeRequest.setTp(barcode.getTp());
            barcodeRequest.setMaterial(barcode.getMaterialCode());
            barcodeRequest.setUm(barcode.getUm());

            listBarcode.add(barcodeRequest);
        }
        return listBarcode;
    }
}
