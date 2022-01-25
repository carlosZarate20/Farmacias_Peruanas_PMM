package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.dto.ResponseDto;

import javax.servlet.http.HttpServletRequest;

public interface ProviderService {

    ResponseDto ejecutarProceso(String typeOp, HttpServletRequest httpSession);
}
