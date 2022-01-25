package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.rest;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.dto.BarcodeDto;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.dto.ResponseDto;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.service.BarcodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
public class BarcodeController {

    @Autowired
    private BarcodeService barcodeService;

    @PostMapping(value = "/enviarCodigoBarra")
    public ResponseDto enviarCodigoBarra(HttpServletRequest httpSession){
        return barcodeService.enviarCodigoBarra(httpSession);
    }

}
