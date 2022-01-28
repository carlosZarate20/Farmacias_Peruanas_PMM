package com.farmaciasperuanas.pmmli.provider.service;

import com.farmaciasperuanas.pmmli.provider.dto.LoginRequest;
import com.farmaciasperuanas.pmmli.provider.dto.LoginResponse;

public interface LoginService {
    String iniciarSession(LoginRequest loginRequest);
}
