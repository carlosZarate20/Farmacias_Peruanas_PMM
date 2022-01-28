package com.farmaciasperuanas.pmmli.materialinformation.rest;

import com.farmaciasperuanas.pmmli.materialinformation.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.materialinformation.service.MaterialLotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class MaterialLotController {
    @Autowired
    private MaterialLotService materialLotService;

    @PostMapping("/enviarMaterialLot")
    public ResponseDto enviarMaterialLot(){
        return materialLotService.enviarMaterialLot();
    }
}
