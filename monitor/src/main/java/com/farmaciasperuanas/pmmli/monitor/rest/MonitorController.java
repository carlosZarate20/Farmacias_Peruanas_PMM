package com.farmaciasperuanas.pmmli.monitor.rest;

import com.farmaciasperuanas.pmmli.monitor.dto.*;
import com.farmaciasperuanas.pmmli.monitor.entity.TransactionTask;
import com.farmaciasperuanas.pmmli.monitor.repository.TransactionTaskRepository;
import com.farmaciasperuanas.pmmli.monitor.service.EndpointService;
import com.farmaciasperuanas.pmmli.monitor.service.TransactionLogService;
import com.farmaciasperuanas.pmmli.monitor.service.TaskSchedulingService;
import com.farmaciasperuanas.pmmli.monitor.task.*;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
  private EndpointService providerService;

  @Autowired
  private TaskSchedulingService taskSchedulingService;

  @Autowired
  private MasterProccessProvider masterProccessProvider;

  @Autowired
  private MasterProccessMasterialProvider masterProccessMaterialProvider;

  @Autowired
  private MasterProccessBarcode masterProccessBarcode;

  @Autowired
  private MasterProccessMaterial masterProccessMaterial;

  @Autowired
  private MasterProccessMaterialLot masterProccessMaterialLot;

  @Autowired
  private MasterProccessStore masterProccessStore;

  @Autowired
  private MasterProccessVolumeticData masterProccessVolumeticData;

  @Autowired
  private TransactionTaskRepository transactionTaskService;

  @RequestMapping("/listaDataMaestra")
  public List<DataMaestraDto> listarDataMaestra() {
    return transactionLogService.getDatosMaestros();
  }

  @PostMapping("/enviar/{typeOp}")
  public ResponseDto ejecutarProceso(@PathVariable("typeOp") String typeOp){
      return providerService.ejecutarProceso(typeOp);
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
      TransactionTask entity = transactionTaskService.getTransactionTaskByCodeWork(dto.getId());
      String jobId = MessageFormat.format("master_process_{0}",dto.getId());
      if (dto.isActivated)
      {
        entity.setTaskState("A");
        Integer hour = dto.getCron().getHours();
        Integer minutes = dto.getCron().getMinutes();
        taskSchedulingService.removeScheduledTask(jobId);
        String cronExpression = MessageFormat.format("0 {0} {1} * * ?", minutes.toString(),hour.toString());
        entity.setCronExpression(cronExpression);
        transactionTaskService.save(entity);
        switch (dto.getId()) {
          case "MM":
            taskSchedulingService.scheduleTask(jobId, masterProccessMaterial, cronExpression );
            break;
          case "MP":
            taskSchedulingService.scheduleTask(jobId, masterProccessProvider, cronExpression );
            break;
          case "MMP":
            taskSchedulingService.scheduleTask(jobId, masterProccessMaterialProvider, cronExpression );
            break;
          case "MML":
            taskSchedulingService.scheduleTask(jobId, masterProccessMaterialLot, cronExpression );
            break;
          case "MVD":
            taskSchedulingService.scheduleTask(jobId, masterProccessVolumeticData, cronExpression );
            break;
          case "MB":
            taskSchedulingService.scheduleTask(jobId, masterProccessBarcode, cronExpression );
            break;
          case "MS":
            taskSchedulingService.scheduleTask(jobId, masterProccessStore, cronExpression );
            break;
          default:
            break;
        }
      } else {
        taskSchedulingService.removeScheduledTask(jobId);
        entity.setTaskState("I");
        transactionTaskService.save(entity);
      }
    }catch (Exception e)
    {
      logger.error("Ocurrió un error al iniciar el job del proceso 1", e);
    }

  }

  @GetMapping("/getTransactionTask/{code}")
  public TransactionTask listarTransactionDashboard(@PathVariable("code") String code){
    return transactionTaskService.getTransactionTaskByCodeWork(code);
  }

  @GetMapping("/getCantTransactionMonth")
  public CantMaestroDto getCantTransactionMonth(){
    return transactionLogService.getCantidadDatosMonth();
  }
}