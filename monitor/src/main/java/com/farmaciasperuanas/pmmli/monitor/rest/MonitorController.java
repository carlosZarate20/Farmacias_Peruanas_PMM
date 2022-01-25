package com.farmaciasperuanas.pmmli.monitor.rest;

import com.farmaciasperuanas.pmmli.monitor.dto.DataMaestraDto;
import com.farmaciasperuanas.pmmli.monitor.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.monitor.service.ProviderService;
import com.farmaciasperuanas.pmmli.monitor.service.TransactionLogService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Controlador principal que expone el servicio a trav&eacute;s de HTTP/Rest para
 * las operaciones del recurso Monitor<br/>
 * <b>Class</b>: MonitorController<br/>
 * <b>Company</b>:Farmacias Peruanas.<br/>
 *
 * @author Inkafarma <br/>
 * <u>Developed by</u>: <br/>
 * <ul>
 * <li>Carlos Zárate Carpio</li>
 * </ul>
 * <u>Changes</u>:<br/>
 * <ul>
 * <li>Jan 19, 2022 Creaci&oacute;n de Clase.</li>
 * </ul>
 * @version 1.0
 */
@Slf4j
@RestController
public class MonitorController {

  @Autowired
  private TransactionLogService transactionLogService;

  @Autowired
  private ProviderService providerService;

  @RequestMapping("/listaDataMaestra")
  public List<DataMaestraDto> listarDataMaestra() {
    return transactionLogService.getDatosMaestros();
  }

  @PostMapping("/enviar/{typeOp}")
  public ResponseDto ejecutarProceso(@PathVariable("typeOp") String typeOp, HttpServletRequest httpSession){
      return providerService.ejecutarProceso(typeOp, httpSession);
  }
}