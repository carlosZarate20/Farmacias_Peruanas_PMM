package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.rest;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto.ResponseDto;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.service.MaterialProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class MaterialProviderController {
    @Autowired
    private MaterialProviderService materialProviderService;

    @PostMapping("/enviarMaterialProvider")
    ResponseDto enviarMaterialProvider(HttpServletRequest httpSession){
        return materialProviderService.enviarMaterialProvider(httpSession);
    }
}
