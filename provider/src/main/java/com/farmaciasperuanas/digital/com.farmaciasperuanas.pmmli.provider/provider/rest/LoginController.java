package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.rest;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto.LoginRequest;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto.LoginResponse;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    /*@Value("${application.url-session}")
    private String urlString;*/

    @PostMapping("/users/signin")
    public LoginResponse iniciarSession(@RequestBody LoginRequest loginRequest){
        String urlString = "https://dev-logisticainversa.solucionesfps.pe/users/signin";
        return loginService.iniciarSession(loginRequest, urlString);
    }
}
