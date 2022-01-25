package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.service;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto.LoginRequest;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto.LoginResponse;

public interface LoginService {
    LoginResponse iniciarSession(LoginRequest loginRequest, String urlString);
}
