package com.farmaciasperuanas.pmmli.merchandisemovements.rest;

import com.farmaciasperuanas.pmmli.merchandisemovements.dto.CdTransferInDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.CdTransferOutDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.service.CdTransferInService;
import com.farmaciasperuanas.pmmli.merchandisemovements.service.CdTransferOutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class CDTransferController {

    @Autowired
    private CdTransferOutService cdTransferOutService;

    @Autowired
    private CdTransferInService cdTransferInService;

    @PostMapping("/cdTransferInPMM")
    public ResponseDto saveCdTransferIn(@RequestBody List<CdTransferInDto> list){
        return cdTransferInService.transferCdIn(list);
    }
    @PostMapping("/cdTransferOutPMM")
    public ResponseDto saveCdTransferOut(@RequestBody List<CdTransferOutDto> list){
        return cdTransferOutService.transferCdOut(list);
    }
}
