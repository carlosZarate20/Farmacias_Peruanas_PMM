package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.localstore.localstore.service;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.localstore.localstore.dto.ResponseDto;

import javax.servlet.http.HttpServletRequest;

public interface StoreService {
    ResponseDto enviarTienda(HttpServletRequest httpSession);
}
