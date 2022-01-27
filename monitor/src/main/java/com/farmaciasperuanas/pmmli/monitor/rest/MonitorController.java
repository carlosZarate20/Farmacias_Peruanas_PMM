package com.farmaciasperuanas.pmmli.monitor.rest;

import com.farmaciasperuanas.pmmli.monitor.dto.*;
import com.farmaciasperuanas.pmmli.monitor.service.ProviderService;
import com.farmaciasperuanas.pmmli.monitor.service.TransactionLogService;
import com.farmaciasperuanas.pmmli.monitor.service.TaskSchedulingService;
import com.farmaciasperuanas.pmmli.monitor.task.MasterProccessTask1;
import com.farmaciasperuanas.pmmli.monitor.task.MasterProccessTask2;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
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
  private static Logger logger = LoggerFactory.getLogger(MonitorController.class);

  @Autowired
  private TransactionLogService transactionLogService;

  @Autowired
  private ProviderService providerService;

  @Autowired
  private TaskSchedulingService taskSchedulingService;

  @Autowired
  private MasterProccessTask1 masterProccessTask1;

  @Autowired
  private MasterProccessTask2 masterProccessTask2;

  @RequestMapping("/listaDataMaestra")
  public List<DataMaestraDto> listarDataMaestra() {
    return transactionLogService.getDatosMaestros();
  }

  @PostMapping("/enviar/{typeOp}")
  public ResponseDto ejecutarProceso(@PathVariable("typeOp") String typeOp, HttpServletRequest httpSession){
      return providerService.ejecutarProceso(typeOp, httpSession);
  }

  @GetMapping("/listarTransactionDashboard")
  public List<TransactionDto> listarTransactionDashboard(){
      return transactionLogService.listarTransactionDashboard();
  }

  @GetMapping("/getDetailTransaction/{id}")
  public TransanctionDetailDto getDetailTransaction(@PathVariable("id") Integer id){
      return transactionLogService.getDetailTransaction(id);
  }

  @PostMapping(path="/initJobProcess")
  public void scheduleTask(@RequestBody TaskCronDto dto) {
    try {

      String jobId = MessageFormat.format("master_process_{0}",dto.getId());
      Integer hour = dto.getCron().getHours();
      Integer minutes = dto.getCron().getMinutes();
      taskSchedulingService.removeScheduledTask(jobId);
      switch (dto.getId()) {
        case 1:
          taskSchedulingService.scheduleTask(jobId, masterProccessTask1, MessageFormat.format("0 {0} {1} * * ?", minutes.toString(),hour.toString()));
          break;
        case 2:
          taskSchedulingService.scheduleTask(jobId, masterProccessTask2, MessageFormat.format("0 {0} {1} * * ?",  minutes.toString(),hour.toString()));
          break;
        case 3:
          taskSchedulingService.scheduleTask(jobId, masterProccessTask1, MessageFormat.format("0 {0} {1} * * ?",  minutes.toString(),hour.toString()));
          break;
        case 4:
          taskSchedulingService.scheduleTask(jobId, masterProccessTask1, MessageFormat.format("0 {0} {1} * * ?",  minutes.toString(),hour.toString()));
          break;
        case 5:
          taskSchedulingService.scheduleTask(jobId, masterProccessTask1, MessageFormat.format("0 {0} {1} * * ?",  minutes.toString(),hour.toString()));
          break;
        case 6:
          taskSchedulingService.scheduleTask(jobId, masterProccessTask1, MessageFormat.format("0 {0} {1} * * ?",  minutes.toString(),hour.toString()));
          break;
        case 7:
          taskSchedulingService.scheduleTask(jobId, masterProccessTask1, MessageFormat.format("0 {0} {1} * * ?",  minutes.toString(),hour.toString()));
          break;
        default:
          break;
      }

    }catch (Exception e)
    {
      logger.error("Ocurrió un error al iniciar el job del proceso 1", e);
    }
  }
}