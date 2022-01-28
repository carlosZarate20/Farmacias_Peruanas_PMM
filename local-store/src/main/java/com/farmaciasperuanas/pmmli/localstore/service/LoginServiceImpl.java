package com.farmaciasperuanas.pmmli.localstore.service;

import com.farmaciasperuanas.pmmli.localstore.dto.LoginRequest;
import com.google.gson.Gson;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Service
public class LoginServiceImpl implements LoginService{
    private static final Logger LOGGER = LogManager.getLogger(LoginServiceImpl.class);

    private static final Gson GSON = new Gson();

    @Override
    public String iniciarSession(LoginRequest loginRequest) {
        String urlString = "https://dev-logisticainversa.solucionesfps.pe/users/signin";
        String token = "";
        try{
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

            String input = GSON.toJson(loginRequest);

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

            token = "Bearer " + sb.toString();
            httpUrlConnection.disconnect();

        } catch (Exception e){
            e.printStackTrace();
        }

        return token;
    }
}
