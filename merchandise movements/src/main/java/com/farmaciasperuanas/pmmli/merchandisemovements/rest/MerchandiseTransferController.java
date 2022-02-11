package com.farmaciasperuanas.pmmli.merchandisemovements.rest;

import com.farmaciasperuanas.pmmli.merchandisemovements.dto.MerchandiseTransferDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.service.MerchandiseTransferService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

  @Autowired
  private MerchandiseTransferService merchandiseTransferService;

  @PostMapping("/merchandiseTransfer")
  public ResponseDto enviarMerchandiseTransfer(@RequestBody List<MerchandiseTransferDto> merchandiseTransferDtoList) {
    return merchandiseTransferService.enviarMerchandiseTransfer(merchandiseTransferDtoList);
  }

}