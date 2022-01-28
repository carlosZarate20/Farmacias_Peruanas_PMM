package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.monitor.util.Constants;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Service
public class EndpointServiceImpl implements EndpointService {

    private static final Gson GSON = new Gson();

    @Override
    public ResponseDto ejecutarProceso(String typeOp) {

        ResponseDto responseDto = new ResponseDto();
        String urlString = "";
        try {
            if(typeOp.equalsIgnoreCase(Constants.TYPE_PROVEEDORES)){
                urlString = Constants.URL_PROVEEDORES;
            } else if (typeOp.equalsIgnoreCase(Constants.TYPE_BARCODE)){
                urlString = Constants.URL_BARCODE;
            } else if(typeOp.equalsIgnoreCase(Constants.TYPE_MATERIAL)){
                urlString = Constants.URL_MATERIAL;
            } else if(typeOp.equalsIgnoreCase(Constants.TYPE_TIENDAS)){
                urlString = Constants.URL_TIENDAS;
            } else if(typeOp.equalsIgnoreCase(Constants.TYPE_DATOS_VOLUMETRICOS)){
                urlString = Constants.URL_DATOS_VOLUMETRICOS;
            } else if(typeOp.equalsIgnoreCase(Constants.TYPE_MATERIAL_LOT)){
                urlString = Constants.URL_MATERIAL_LOT;
            } else if(typeOp.equalsIgnoreCase(Constants.TYPE_MATERIAL_PROVEEDOR)){
                urlString = Constants.URL_MATERIAL_PROVEEDOR;
            }

            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();

            HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
            httpUrlConnection.setConnectTimeout(30_000);
            httpUrlConnection.setReadTimeout(30_000);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestProperty("Content-Type", "application/json");
            httpUrlConnection.setRequestProperty("Accept", "application/json");
            httpUrlConnection.setRequestMethod("POST");
            OutputStream os = httpUrlConnection.getOutputStream();
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(), StandardCharsets.UTF_8));
            String outPut;
            StringBuilder sb = new StringBuilder();

            while((outPut = br.readLine()) != null) {
                sb.append(outPut);
            }
            responseDto = GSON.fromJson(sb.toString(), ResponseDto.class);
            httpUrlConnection.disconnect();

        } catch (Exception e){
            e.printStackTrace();
        }
        return responseDto;
    }
}
