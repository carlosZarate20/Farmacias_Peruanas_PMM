package com.farmaciasperuanas.pmmli.merchandisemovements.service;

import com.farmaciasperuanas.pmmli.merchandisemovements.dto.CdTransferOutDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CdTransferOutService {
    ResponseDto transferCdOut(List<CdTransferOutDto> providerExitDtoList);
}
