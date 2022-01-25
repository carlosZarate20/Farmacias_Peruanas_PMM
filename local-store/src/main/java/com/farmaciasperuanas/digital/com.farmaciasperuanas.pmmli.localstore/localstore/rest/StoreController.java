package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.localstore.localstore.rest;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.localstore.localstore.dto.ResponseDto;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.localstore.localstore.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class StoreController {
    @Autowired
    private StoreService storeService;

    @PostMapping("/enviarStore")
    public ResponseDto enviarTienda(HttpServletRequest httpSession){
        return storeService.enviarTienda(httpSession);
    }
}
