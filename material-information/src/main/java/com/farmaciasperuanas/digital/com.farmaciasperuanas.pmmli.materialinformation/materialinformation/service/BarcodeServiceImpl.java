package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.service;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.dto.ResponseDto;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.dto.BarcodeDto;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.dto.BarcodeResponse;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.dto.ResponseApi;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.dto.ResponseApiError;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.dto.ResponseApiForbidden;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.entity.Barcode;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.repository.BarcodeRepository;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.repository.TransactionLogRepository;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BarcodeServiceImpl implements BarcodeService{
    @Autowired
    private BarcodeRepository barcodeRepository;

    private static final Logger LOGGER = LogManager.getLogger(BarcodeServiceImpl.class);

    private static final Gson GSON = new Gson();

    @Autowired
    private TransactionLogService transactionLogService;

    @Override
    public ResponseDto enviarCodigoBarra(HttpServletRequest httpSession) {
        BarcodeResponse barcodeResponse = new BarcodeResponse();
        ResponseDto responseDto = new ResponseDto();
        ResponseApi responseApi = new ResponseApi();
        ResponseApiError responseApiError = new ResponseApiError();
        ResponseApiForbidden responseApiForbidden = new ResponseApiForbidden();

        List<BarcodeDto> listBarcode = new ArrayList<>();

        String urlString = "https://dev-logisticainversa.solucionesfps.pe/master_table/load_master_barcode";

        String responseBody = "";
        String requestBody = "";
        try {
            listBarcode = getListBarcode();
            if(listBarcode.size() != 0){
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
            /*if(httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                responseApi = GSON.fromJson(sb.toString(), ResponseApi.class);
                responseDto.setBody(responseApi);

            } else if (httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN){
                responseApiForbidden = GSON.fromJson(sb.toString(), ResponseApiForbidden.class);
                responseDto.setBody(responseApiForbidden);

            } else if (httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR){
                responseApiError = GSON.fromJson(sb.toString(), ResponseApiError.class);
                responseDto.setBody(responseApiError);
            }*/
                responseApi = GSON.fromJson(sb.toString(), ResponseApi.class);
                httpUrlConnection.disconnect();

                if(responseApi.getCode().equalsIgnoreCase("ok")){

                    for(BarcodeDto barcodeDto: listBarcode){
                        barcodeRepository.updateBarcode(barcodeDto.getMaterial());
                    }

                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(true);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Registro Correcto");
                } else {
                    responseDto.setCode(HttpStatus.OK.value());
                    responseDto.setStatus(false);
                    responseDto.setBody(responseApi);
                    responseDto.setMessage("Ocurrio error");
                }

                responseBody = String.valueOf(responseDto);
                requestBody = GSON.toJson(listBarcode);

                transactionLogService.saveTransactionLog("Maestro Barcode", "M",
                        "MB", "Data Maestra",
                        responseDto.isStatus(), requestBody, responseBody);
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
