package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.monitor.dto.ResponseMasterTransactionDto;

import javax.servlet.http.HttpServletRequest;

public interface EndpointService {

    ResponseMasterTransactionDto ejecutarProceso(String typeOp);
}
