package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.service;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.dto.BarcodeDto;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.dto.ResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BarcodeService {
    ResponseDto enviarCodigoBarra(HttpServletRequest httpSession);
}
