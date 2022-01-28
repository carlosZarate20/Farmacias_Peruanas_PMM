package com.farmaciasperuanas.pmmli.localstore.rest;

import com.farmaciasperuanas.pmmli.localstore.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.localstore.service.StoreService;
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
    public ResponseDto enviarTienda(){
        return storeService.enviarTienda();
    }
}
