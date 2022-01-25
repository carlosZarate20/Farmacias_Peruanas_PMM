package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.service;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.dto.ResponseDto;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.dto.VolumetricDataDto;

import javax.servlet.http.HttpServletRequest;

public interface VolumetricDataService {
    ResponseDto enviarDataVolumetrica(HttpServletRequest httpSession);
}
