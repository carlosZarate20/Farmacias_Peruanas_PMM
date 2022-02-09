package com.farmaciasperuanas.pmmli.merchandisemovements.service;

import com.farmaciasperuanas.pmmli.merchandisemovements.dto.CdTransferInDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ResponseDto;

import java.util.List;

public interface CdTransferInService {
    ResponseDto transferCdIn(List<CdTransferInDto> providerExitDtoList);
}
