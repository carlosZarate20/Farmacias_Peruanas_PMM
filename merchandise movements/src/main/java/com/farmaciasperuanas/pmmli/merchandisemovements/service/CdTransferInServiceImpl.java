package com.farmaciasperuanas.pmmli.merchandisemovements.service;

import com.farmaciasperuanas.pmmli.merchandisemovements.dto.CdTransferInDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ErrorsDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.CdTransferIn;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.TransactionLogError;
import com.farmaciasperuanas.pmmli.merchandisemovements.repository.CdTransferInRepository;
import com.farmaciasperuanas.pmmli.merchandisemovements.repository.TransactionLogErrorRepository;
import com.farmaciasperuanas.pmmli.merchandisemovements.repository.TransactionLogRepository;
import com.farmaciasperuanas.pmmli.merchandisemovements.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CdTransferInServiceImpl implements CdTransferInService {
    private static final Gson GSON = new Gson();

    @Autowired
    private CdTransferInRepository cdTransferInRepository;

    @Autowired
    private TransactionLogService transactionLogService;

    @Autowired
    private TransactionLogErrorRepository transactionLogErrorRepository;

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    public ResponseDto transferCdIn(List<CdTransferInDto> providerExitDtoList) {
        ResponseDto responseDto = new ResponseDto();

        Integer sessionNumber = 0;

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormat = dt.format(new Date());
        Date dateCreate = new Date(dateFormat);

        String responseBody = "";
        String requestBody = "";
        String status = "";
        Integer contTechKey = 1;
        List<ErrorsDto> errorsDtoList = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            //obtenemos el correlativo session_number
            sessionNumber = cdTransferInRepository.getSessionNumber();

//            //insertamos en nuestra tabla intermedia
            for (CdTransferInDto dto : providerExitDtoList) {
                CdTransferIn cdTransferIn = new CdTransferIn();
                cdTransferIn.setSessionNumber(sessionNumber);
                cdTransferIn.setTechKey(contTechKey);
                cdTransferIn.setTrfNumber(dto.getTrfNumber());
                cdTransferIn.setCarrierName(dto.getCarrierName().isEmpty() ? null : dto.getCarrierName());
                cdTransferIn.setCartonNumber(dto.getCartonNumber().isEmpty() ? null : dto.getCartonNumber());
                cdTransferIn.setPrdLvlNumber(dto.getPrdLvlNumber());
                cdTransferIn.setJdaOrigin(Constants.JDA_ORIGIN);
                cdTransferIn.setTrfReasonCode(dto.getTrfReasonCode());
                cdTransferIn.setFromLoc(Constants.FROM_LOC);
                cdTransferIn.setToLoc(dto.getToLoc());
                cdTransferIn.setQuantity(dto.getQuantity());
                cdTransferIn.setActionCode(Constants.ACTION_CODE);
                cdTransferIn.setDateCreated(dateCreate);
                cdTransferIn.setRequestedBy(dto.getRequestedBy());
                cdTransferIn.setInnerPackId(cdTransferInRepository.getInnerPack(cdTransferIn.getPrdLvlNumber()));
                cdTransferIn.setTrfQtyFlag(Constants.TRF_QTY_FLAG);
                cdTransferIn.setTrfSourceId(Constants.TRF_SOURCE_ID);
                cdTransferIn.setTrfLote(dto.getTrfLote());
                cdTransferIn.setTrfVctoLote(dto.getTrfVctoLote());


//
                cdTransferInRepository.save(cdTransferIn);
                contTechKey++;
            }
            //consumimos el procedure para insertar los datos en pmm
            cdTransferInRepository.storeProcedure(sessionNumber);

            //validamos errores y hacemos insert
            Integer validateExitsError = cdTransferInRepository.validateExitsError(sessionNumber);
//
            if(validateExitsError != 0){
                requestBody = mapper.writeValueAsString(providerExitDtoList);
                status = providerExitDtoList.size() == validateExitsError ? "F" : "FP";

                responseDto.setStatus(false);
                responseDto.setMessage(status.equalsIgnoreCase("F")  ? Constants.MESSAGE_FALLO_TOTAL_TRANSACTION : Constants.MESSAGE_FALLO_PARCIAL_TRANSACTION);
                responseDto.setCode(HttpStatus.OK.value());

                responseBody = mapper.writeValueAsString(responseDto);

                TransactionLog tl = transactionLogService.saveTransactionLog(Constants.NAME_TRANSACTION_IN,
                        "T", "CTI", "Transaccion" ,status , requestBody , responseBody);

                cdTransferInRepository.storeProcedureLogError(tl.getIdTransacctionLog(), sessionNumber, "Código del material: ", ", Nro Lote: ","CDT");

                List<TransactionLogError> transactionLogErrorList = transactionLogErrorRepository.getTransactionLogError(tl.getIdTransacctionLog());
                for(TransactionLogError transactionLogError: transactionLogErrorList){
                    ErrorsDto errorsDto = new ErrorsDto();
                    errorsDto.setIdentifier(transactionLogError.getIdentifier().trim());
                    errorsDto.setMessage(transactionLogError.getMessage());
                    errorsDtoList.add(errorsDto);
                }

                responseDto.setBody(errorsDtoList);
                responseBody = mapper.writeValueAsString(responseDto);
                transactionLogRepository.updateTransaction(responseBody,tl.getIdTransacctionLog());

            } else {
                requestBody = mapper.writeValueAsString(providerExitDtoList);
                status = "C";

                responseDto.setStatus(false);
                responseDto.setMessage(Constants.MESSAGE_OK_TRANSACTION);
                responseDto.setCode(HttpStatus.OK.value());
                responseBody = mapper.writeValueAsString(responseDto);

                transactionLogService.saveTransactionLog(Constants.NAME_TRANSACTION_IN,
                        "T", "CTI", "Transaccion",status, requestBody, responseBody);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseDto;
    }
}
