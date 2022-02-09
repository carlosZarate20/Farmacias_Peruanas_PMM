package com.farmaciasperuanas.pmmli.localstore.rest;

import com.farmaciasperuanas.pmmli.localstore.dto.LocalReturnDto;
import com.farmaciasperuanas.pmmli.localstore.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.localstore.service.LocalReturnService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador principal que expone el servicio a trav&eacute;s de HTTP/Rest para
 * las operaciones del recurso Localstore<br/>
 * <b>Class</b>: LocalstoreController<br/>
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
public class LocalReturnController {

  @Autowired
  private LocalReturnService localReturnService;

  @PostMapping("/enviarExtornosLiPMM")
  public ResponseDto enviarExtornosLiPMM(@RequestBody List<LocalReturnDto> localReturnDtoList) {
    return localReturnService.enviarExtornosLi(localReturnDtoList);
  }

}