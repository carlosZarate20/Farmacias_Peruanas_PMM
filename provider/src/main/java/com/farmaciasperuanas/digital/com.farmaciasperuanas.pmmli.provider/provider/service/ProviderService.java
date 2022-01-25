package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.service;


import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto.ResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProviderService {
    ResponseDto registrarProveedorLi(HttpServletRequest httpSession);
}
