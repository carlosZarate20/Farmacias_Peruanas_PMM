package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.service;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto.LoginRequest;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto.LoginResponse;
import com.google.gson.Gson;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputFilter;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class LoginServiceImpl implements LoginService{

    private static final Logger LOGGER = LogManager.getLogger(LoginServiceImpl.class);

    private static final Gson GSON = new Gson();

    @Override
    public LoginResponse iniciarSession(LoginRequest loginRequest, String urlString) {

        Properties props = new Properties();
        LoginResponse loginResponse = new LoginResponse();
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
            /*httpUrlConnection.setRequestProperty("applicationCode", applicationCode);
            httpUrlConnection.setRequestProperty("transactionId", transactionId);*/
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

            loginResponse.setToken(sb.toString());
            //loginResponse = GSON.fromJson(sb.toString(), LoginResponse.class);
            httpUrlConnection.disconnect();

        } catch (Exception e){
            e.printStackTrace();
        }

        return loginResponse;
    }
}
