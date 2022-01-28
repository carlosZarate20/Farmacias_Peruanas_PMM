package com.farmaciasperuanas.pmmli.localstore.service;

import com.farmaciasperuanas.pmmli.localstore.dto.LoginRequest;

public interface LoginService {
    String iniciarSession(LoginRequest loginRequest);
}
