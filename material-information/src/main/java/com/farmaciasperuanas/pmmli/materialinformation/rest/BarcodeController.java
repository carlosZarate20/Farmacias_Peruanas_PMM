package com.farmaciasperuanas.pmmli.materialinformation.rest;

import com.farmaciasperuanas.pmmli.materialinformation.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.materialinformation.service.BarcodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class BarcodeController {

    @Autowired
    private BarcodeService barcodeService;

    @PostMapping(value = "/enviarCodigoBarra")
    public ResponseDto enviarCodigoBarra(){
        return barcodeService.enviarCodigoBarra();
    }

}
