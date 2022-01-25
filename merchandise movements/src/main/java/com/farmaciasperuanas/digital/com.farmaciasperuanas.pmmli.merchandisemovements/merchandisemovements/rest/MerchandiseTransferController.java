package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.merchandisemovements.merchandisemovements.rest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador principal que expone el servicio a trav&eacute;s de HTTP/Rest para
 * las operaciones del recurso Merchandisemovements<br/>
 * <b>Class</b>: MerchandisemovementsController<br/>
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
public class MerchandiseTransferController {

  @RequestMapping("/merchandisemovementss")
  public String dummy() {
    return "This is a dummy method";
  }

}