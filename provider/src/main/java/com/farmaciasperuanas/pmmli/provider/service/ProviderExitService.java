package com.farmaciasperuanas.pmmli.provider.service;

import com.farmaciasperuanas.pmmli.provider.dto.ProviderExitDto;
import com.farmaciasperuanas.pmmli.provider.dto.ResponseDto;

import java.util.List;

public interface ProviderExitService {
    ResponseDto guardarSalidaProveedorPMM(List<ProviderExitDto> providerExitDtoList);
}
