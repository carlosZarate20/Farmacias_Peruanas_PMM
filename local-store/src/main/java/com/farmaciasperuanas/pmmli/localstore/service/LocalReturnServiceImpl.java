package com.farmaciasperuanas.pmmli.localstore.service;

import com.farmaciasperuanas.pmmli.localstore.dto.ErrorsDto;
import com.farmaciasperuanas.pmmli.localstore.dto.LocalReturnDto;
import com.farmaciasperuanas.pmmli.localstore.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.localstore.entity.CodErrorSdi;
import com.farmaciasperuanas.pmmli.localstore.entity.LocalReturn;
import com.farmaciasperuanas.pmmli.localstore.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.localstore.entity.TransactionLogError;
import com.farmaciasperuanas.pmmli.localstore.repository.CodErrorSdiRepository;
import com.farmaciasperuanas.pmmli.localstore.repository.LocalReturnRepository;
import com.farmaciasperuanas.pmmli.localstore.repository.TransactionLogErrorRepository;
import com.farmaciasperuanas.pmmli.localstore.repository.TransactionLogRepository;
import com.farmaciasperuanas.pmmli.localstore.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocalReturnServiceImpl implements LocalReturnService{
    @Autowired
    private LocalReturnRepository localReturnRepository;

    @Autowired
    private TransactionLogService transactionLogService;

    @Autowired
    private CodErrorSdiRepository codErrorSdiRepository;

    @Autowired
    private TransactionLogErrorService transactionLogErrorService;

    @Override
    public ResponseDto enviarExtornosLi(List<LocalReturnDto> localReturnDtoList) {
        ResponseDto responseDto = new ResponseDto();

        Integer sessionNumber = 0;
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        String responseBody = "";
        String requestBody = "";
        String status = "";
        Integer contTechKey = 1;
        List<ErrorsDto> errorsDtoList = new ArrayList<>();
        List<ErrorsDto> listResponseBody = new ArrayList<>();
        try{
            ObjectMapper mapper = new ObjectMapper();

            //obtenemos el correlativo session_number
            sessionNumber = localReturnRepository.getSessioNumber();

            for(LocalReturnDto localReturnDto: localReturnDtoList){
                LocalReturn localReturn = new LocalReturn();
                localReturn.setSessionNumber(sessionNumber);
                localReturn.setTechKey(contTechKey);
                localReturn.setCarrierName(localReturnDto.getCarrierName());
                localReturn.setCartonNumber(localReturnDto.getCartonNumber());
                localReturn.setPrdLvlNumber(localReturnDto.getPrdLvlNumber());
                localReturn.setJdaOrigin(Constants.JDA_ORIGIN);
                localReturn.setTrfReasonCode(Constants.TRF_REASON_CODE);
                localReturn.setFromLoc(Constants.FROM_LOC);
                localReturn.setToLoc(localReturnDto.getToLoc());
                localReturn.setQuantity(localReturnDto.getQuantity());
                localReturn.setActionCode(Constants.ACTION_CODE);
                localReturn.setDateCreated(localReturnDto.getDateCreated());
                localReturn.setRequestedBy(localReturnDto.getRequestedBy());
                localReturn.setInnerPackId(localReturnRepository.getInnerPack(localReturnDto.getPrdLvlNumber()));
                localReturn.setTrfSourceId(Constants.TRF_SOURCE_ID);
                localReturn.setTrfLote(localReturnDto.getTrfLote());
                localReturn.setTrfVctoLote(localReturnDto.getTrfVctoLote());

                localReturnRepository.save(localReturn);

                contTechKey++;
            }
            //consumimos el procedure para insertar los datos en pmm
            localReturnRepository.storeProcedureLocalReturn(sessionNumber);

            //validamos errores y hacemos insert
            Integer validateExitsError = localReturnRepository.validateExitsError(sessionNumber);

            if(validateExitsError != 0){

                List<CodErrorSdi> errorSdiList = codErrorSdiRepository.listError(sessionNumber, "LR");
                for (CodErrorSdi codErrorSdi: errorSdiList) {
                    LocalReturn localReturn = localReturnRepository.getLocalReturn(sessionNumber, codErrorSdi.getTechKey());

                    ErrorsDto errorsDtoBody = new ErrorsDto();
                    String identificador = "Codigo Material: " + localReturn.getPrdLvlNumber() + ", Nro Lote: " + localReturn.getTrfLote();
                    errorsDtoBody.setIdentifier(identificador);
                    errorsDtoBody.setMessage(codErrorSdi.getRejDesc());
                    errorsDtoList.add(errorsDtoBody);

                    ErrorsDto errorsDtoLi = new ErrorsDto();
                    errorsDtoLi.setIdentifier(localReturn.getTrfSourceId());
                    errorsDtoLi.setMessage(codErrorSdi.getRejDesc());
                    listResponseBody.add(errorsDtoLi);
                }

                requestBody = mapper.writeValueAsString(localReturnDtoList);
                status = localReturnDtoList.size() == validateExitsError ? "F" : "FP";

                responseDto.setStatus(false);
                responseDto.setMessage(status.equalsIgnoreCase("F")  ? Constants.MESSAGE_FALLO_TOTAL_TRANSACTION : Constants.MESSAGE_FALLO_PARCIAL_TRANSACTION);
                responseDto.setCode(HttpStatus.OK.value());
                responseDto.setBody(listResponseBody);

                responseBody = mapper.writeValueAsString(responseDto);

                TransactionLog tl = transactionLogService.saveTransactionLog(Constants.NAME_TRANSACTION,
                        "T", "LR", "Transaccion" ,status , requestBody , responseBody);

                for(ErrorsDto errorsDto: errorsDtoList){
                    transactionLogErrorService.saveTransactionLogError(tl,errorsDto.getIdentifier(),errorsDto.getMessage());
                }

            } else {
                requestBody = mapper.writeValueAsString(localReturnDtoList);
                status = "C";

                responseDto.setStatus(true);
                responseDto.setMessage(Constants.MESSAGE_OK_TRANSACTION);
                responseDto.setCode(HttpStatus.OK.value());
                responseBody = mapper.writeValueAsString(responseDto);

                transactionLogService.saveTransactionLog(Constants.NAME_TRANSACTION,
                        "T", "LR", "Transaccion",status, requestBody, responseBody);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return responseDto;
    }
}
