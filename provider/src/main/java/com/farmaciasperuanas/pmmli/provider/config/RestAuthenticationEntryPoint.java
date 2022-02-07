package com.farmaciasperuanas.pmmli.provider.config;

import com.farmaciasperuanas.pmmli.provider.dto.ResponseApi;
import com.farmaciasperuanas.pmmli.provider.dto.ResponseDto;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Gson GSON = new Gson();

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setCode(HttpStatus.UNAUTHORIZED.value());
        responseDto.setStatus(false);
        responseDto.setBody(new ResponseApi());
        responseDto.setMessage("Usuario no autorizado");
        String input = GSON.toJson(responseDto);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(input);

    }
}