package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.service;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.dto.MaterialLotDto;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.dto.ResponseDto;

import javax.servlet.http.HttpServletRequest;

public interface MaterialLotService {
    ResponseDto enviarMaterialLot(HttpServletRequest httpSession);
}
