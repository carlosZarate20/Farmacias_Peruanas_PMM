package com.farmaciasperuanas.pmmli.materialinformation.rest;

import com.farmaciasperuanas.pmmli.materialinformation.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.materialinformation.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @PostMapping("/enviarMaterial")
    public ResponseDto enviarMaterial(){
        return materialService.enviarMaterial();
    }
}
