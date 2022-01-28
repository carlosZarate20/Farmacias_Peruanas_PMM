package com.farmaciasperuanas.pmmli.materialinformation.service;

import com.farmaciasperuanas.pmmli.materialinformation.dto.LoginRequest;

public interface LoginService {
    String iniciarSession(LoginRequest loginRequest);
}
