package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.rest;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto.ResponseDto;
import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.service.ProviderService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Controlador principal que expone el servicio a trav&eacute;s de HTTP/Rest para
 * las operaciones del recurso Provider<br/>
 * <b>Class</b>: ProviderController<br/>
 * <b>Company</b>:Farmacias Peruanas.<br/>
 *
 * @author Inkafarma <br/>
 * <u>Developed by</u>: <br/>
 * <ul>
 * <li>Carlos Zárate Carpio</li>
 * </ul>
 * <u>Changes</u>:<br/>
 * <ul>
 * <li>Dec 28, 2021 Creaci&oacute;n de Clase.</li>
 * </ul>
 * @version 1.0
 */
@Slf4j
@RestController
public class ProviderController {

  @Autowired
  private ProviderService providerService;


  @PostMapping("/enviarProveedor")
  public ResponseDto enviarProveedorLi(HttpServletRequest httpSession){
    return providerService.registrarProveedorLi(httpSession);
  }

}