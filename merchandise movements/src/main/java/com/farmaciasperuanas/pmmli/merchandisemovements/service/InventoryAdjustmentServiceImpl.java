package com.farmaciasperuanas.pmmli.merchandisemovements.service;

import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ErrorsDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.InventoryAdjustmentDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.CodErrorSdi;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.InventoryAdjustment;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.merchandisemovements.repository.CodErrorSdiRepository;
import com.farmaciasperuanas.pmmli.merchandisemovements.repository.InventoryAdjustmentRepository;
import com.farmaciasperuanas.pmmli.merchandisemovements.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InventoryAdjustmentServiceImpl implements InventoryAdjustmentService {

    @Autowired
    private InventoryAdjustmentRepository inventoryAdjustmentRepository;

    @Autowired
    private TransactionLogService transactionLogService;

    @Autowired
    private CodErrorSdiRepository codErrorSdiRepository;

    @Autowired
    private TransactionLogErrorService transactionLogErrorService;

    @Override
    public ResponseDto enviarControlInventarioLi(List<InventoryAdjustmentDto> inventoryAdjustmentDtoList) {
        ResponseDto responseDto = new ResponseDto();

        Integer transSession = 0;

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");

        String responseBody = "";
        String requestBody = "";
        String status = "";
        Integer contTransSequence = 1;
        List<ErrorsDto> errorsDtoList = new ArrayList<>();
        List<ErrorsDto> listResponseBody = new ArrayList<>();

        try{
            ObjectMapper mapper = new ObjectMapper();
            //obtenemos el correlativo session_number
            transSession = inventoryAdjustmentRepository.getTransSession();

            //insertamos en nuestra tabla intermedia
            for(InventoryAdjustmentDto inventoryAdjustmentDto: inventoryAdjustmentDtoList){
                InventoryAdjustment inventoryAdjustment = new InventoryAdjustment();
                inventoryAdjustment.setTransSession(transSession);
                inventoryAdjustment.setTransUser(inventoryAdjustmentDto.getTransUser());
                inventoryAdjustment.setTransBatchDate(new Date());
                inventoryAdjustment.setTransSource(Constants.TRANS_SOURCE_CI);
                inventoryAdjustment.setTransAudited(Constants.TRANS_AUDITED);
                inventoryAdjustment.setTransSequence(contTransSequence);
                inventoryAdjustment.setTransTrnCode(inventoryAdjustmentDto.getTransTrnCode());
                inventoryAdjustment.setTransTypeCode(inventoryAdjustmentDto.getTransTypeCode());
                String dateFormat = dt.format(inventoryAdjustmentDto.getTransDate());
                Date dateTrans = dt.parse(dateFormat);
                inventoryAdjustment.setTransDate(dateTrans);
                inventoryAdjustment.setInvMrptCode(inventoryAdjustmentDto.getInvMrptCode());
                inventoryAdjustment.setInvDrptCode(inventoryAdjustmentDto.getInvDrptCode());
                inventoryAdjustment.setTransCurrCode(Constants.TRANS_CURR_CODE);
                inventoryAdjustment.setTransOrgLvlNumber(Constants.TRANS_ORG_LVL_NUMBER);
                inventoryAdjustment.setTransPrdLvlNumber(inventoryAdjustmentDto.getTransPrdLvlNumber());
                inventoryAdjustment.setProcSource(inventoryAdjustmentDto.getProcSource());
                inventoryAdjustment.setTransQty(inventoryAdjustmentDto.getTransQty());
                inventoryAdjustment.setInnerPackId(inventoryAdjustmentRepository.getInnerPack(inventoryAdjustment.getTransPrdLvlNumber()));
                inventoryAdjustment.setTransInners(inventoryAdjustmentDto.getTransInners());
                inventoryAdjustment.setTransLote(inventoryAdjustmentDto.getTransLote());
                inventoryAdjustment.setTransVctoLote(inventoryAdjustmentDto.getTransVctoLote());

                inventoryAdjustmentRepository.save(inventoryAdjustment);

                contTransSequence++;
            }

            //consumimos el procedure para insertar los datos en pmm
            inventoryAdjustmentRepository.storeProcedure(transSession);

            //validamos errores y hacemos insert
            Integer validateExitsError = inventoryAdjustmentRepository.validateExitsError(transSession);

            if(validateExitsError != 0){
                List<CodErrorSdi> errorSdiList = codErrorSdiRepository.listError(transSession, "IA");
                for (CodErrorSdi codErrorSdi: errorSdiList) {
                    InventoryAdjustment inventoryAdjustment = inventoryAdjustmentRepository.getInventoryAdjustment(transSession, codErrorSdi.getTechKey());

                    ErrorsDto errorsDtoBody = new ErrorsDto();
                    String identificador = "Codigo Material: " + inventoryAdjustment.getTransPrdLvlNumber() + ", Nro Lote: " + inventoryAdjustment.getTransLote();
                    errorsDtoBody.setIdentifier(identificador);
                    errorsDtoBody.setMessage(codErrorSdi.getRejDesc());
                    errorsDtoList.add(errorsDtoBody);

                    ErrorsDto errorsDtoLi = new ErrorsDto();
                    errorsDtoLi.setIdentifier(inventoryAdjustment.getTransSource());
                    errorsDtoLi.setMessage(codErrorSdi.getRejDesc());
                    listResponseBody.add(errorsDtoLi);
                }

                requestBody = mapper.writeValueAsString(errorSdiList);
                status = inventoryAdjustmentDtoList.size() == validateExitsError ? "F" : "FP";

                responseDto.setStatus(false);
                responseDto.setMessage(status.equalsIgnoreCase("F")  ? Constants.MESSAGE_FALLO_TOTAL_TRANSACTION : Constants.MESSAGE_FALLO_PARCIAL_TRANSACTION);
                responseDto.setCode(HttpStatus.OK.value());
                responseDto.setBody(listResponseBody);

                responseBody = mapper.writeValueAsString(responseDto);

                TransactionLog tl = transactionLogService.saveTransactionLog(Constants.NAME_INVENTORY_ADJUSTMENT,
                        "T", "IA", "Transaccion" ,status , requestBody , responseBody,transSession);

                for(ErrorsDto errorsDto: errorsDtoList){
                    transactionLogErrorService.saveTransactionLogError(tl,errorsDto.getIdentifier(),errorsDto.getMessage());
                }
            }else {
                requestBody = mapper.writeValueAsString(inventoryAdjustmentDtoList);
                status = "C";

                responseDto.setStatus(true);
                responseDto.setMessage(Constants.MESSAGE_OK_TRANSACTION);
                responseDto.setCode(HttpStatus.OK.value());
                responseBody = mapper.writeValueAsString(responseDto);

                transactionLogService.saveTransactionLog(Constants.NAME_INVENTORY_ADJUSTMENT,
                        "T", "IA", "Transaccion",status, requestBody, responseBody,transSession);
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return responseDto;
    }
}
