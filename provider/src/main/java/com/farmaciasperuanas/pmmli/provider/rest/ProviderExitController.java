package com.farmaciasperuanas.pmmli.provider.rest;

import com.farmaciasperuanas.pmmli.provider.dto.ProviderExitDto;
import com.farmaciasperuanas.pmmli.provider.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.provider.service.ProviderExitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
public class ProviderExitController {

    @Autowired
    private ProviderExitService providerExitService;

    @PostMapping("/guardarSalidaProveedorPMM")
    public ResponseDto guardarSalidaProveedorPMM(@RequestBody List<ProviderExitDto> providerExitDtoList){
        return providerExitService.guardarSalidaProveedorPMM(providerExitDtoList);
    }
}
