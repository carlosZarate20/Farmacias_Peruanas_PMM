package com.farmaciasperuanas.pmmli.materialinformation.service;

import com.farmaciasperuanas.pmmli.materialinformation.dto.LoginRequest;
import com.farmaciasperuanas.pmmli.materialinformation.dto.ResponseApi;
import com.farmaciasperuanas.pmmli.materialinformation.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.materialinformation.dto.VolumetricDataDto;
import com.farmaciasperuanas.pmmli.materialinformation.entity.VolumetricData;
import com.farmaciasperuanas.pmmli.materialinformation.repository.VolumetricDataRepository;
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
public class VolumetricDataServiceImpl implements VolumetricDataService {

    private static final Logger LOGGER = LogManager.getLogger(BarcodeServiceImpl.class);

    private static final Gson GSON = new Gson();

    @Autowired
    private VolumetricDataRepository volumetricDataRepository;

    @Autowired
    private TransactionLogService transactionLogService;

    @Autowired
    private LoginService loginService;

    @Override
    public ResponseDto enviarDataVolumetrica() {

        List<VolumetricDataDto> volumetricDataDtoList = new ArrayList<>();
        ResponseDto responseDto = new ResponseDto();
        ResponseApi responseApi = new ResponseApi();

        String urlString = "https://dev-logisticainversa.solucionesfps.pe/master_table/load_master_barcode";

        String responseBody = "";
        String requestBody = "";

        String authTokenHeader = "";
        LoginRequest loginRequest = new LoginRequest();

        String status = "";
        try{
            volumetricDataDtoList = getListVolumetricData();
            if(volumetricDataDtoList.size() != 0){
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

                String input = GSON.toJson(volumetricDataDtoList);

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
                    for(VolumetricDataDto volumetricDataDto: volumetricDataDtoList){
                        volumetricDataRepository.updateVolumetricData(volumetricDataDto.getMaterial());
                    }
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

                responseBody = String.valueOf(responseApi);
                requestBody = GSON.toJson(volumetricDataDtoList);

                transactionLogService.saveTransactionLog("Maestro Volumetric Data", "M",
                        "MVD", "Data Maestra",
                        status, requestBody, responseBody);

            } else {
                responseDto.setCode(HttpStatus.OK.value());
                responseDto.setStatus(false);
                responseDto.setBody(responseApi);
                responseDto.setMessage("No existen registros en la tabla SWLI.VOLUMETRIC_DATA");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return responseDto;
    }

    private List<VolumetricDataDto> getListVolumetricData(){
        List<VolumetricDataDto> volumetricDataDtoList = new ArrayList<>();
        List<VolumetricData> volumetricDataList = new ArrayList<>();
        volumetricDataList = volumetricDataRepository.getVolumetricDataList();

        for(VolumetricData volumetricData : volumetricDataList) {
            VolumetricDataDto volumetricDataDto = new VolumetricDataDto();

            volumetricDataDto.setAltura(volumetricData.getHeight());
            volumetricDataDto.setAncho(volumetricData.getWidth());
            volumetricDataDto.setContador(0.0);
            volumetricDataDto.setDemon(0.0);
            volumetricDataDto.setEanUpc(volumetricData.getEanUpc());
            volumetricDataDto.setLongitud(volumetricData.getLength());
            volumetricDataDto.setMaterial(volumetricData.getMaterialCode());
            volumetricDataDto.setPesoBruto(volumetricData.getGrossWeight());
            volumetricDataDto.setTp(volumetricData.getTp());
            volumetricDataDto.setUma(volumetricData.getUma());
            volumetricDataDto.setUnidad(volumetricData.getUnit());
            volumetricDataDto.setUnidadPeso(volumetricData.getWeightUnit());
            volumetricDataDto.setUnidadVolumen(volumetricData.getUv());

            volumetricDataDtoList.add(volumetricDataDto);
        }

        return volumetricDataDtoList;
    }
}
