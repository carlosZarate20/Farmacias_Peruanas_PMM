package com.farmaciasperuanas.pmmli.merchandisemovements.service;

import com.farmaciasperuanas.pmmli.merchandisemovements.dto.CdTransferOutDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ErrorsDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.CdTransferOut;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.CodErrorSdi;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.TransactionLogError;
import com.farmaciasperuanas.pmmli.merchandisemovements.repository.CdTransferOutRepository;
import com.farmaciasperuanas.pmmli.merchandisemovements.repository.CodErrorSdiRepository;
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
public class CdTransferOutServiceImpl implements CdTransferOutService {
    private static final Gson GSON = new Gson();

    @Autowired
    private CdTransferOutRepository cdTransferOutRepository;

    @Autowired
    private TransactionLogService transactionLogService;

    @Autowired
    private CodErrorSdiRepository codErrorSdiRepository;

    @Autowired
    private TransactionLogErrorService transactionLogErrorService;

    public ResponseDto transferCdOut(List<CdTransferOutDto> providerExitDtoList) {
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
        List<ErrorsDto> listResponseBody = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            //obtenemos el correlativo session_number
            sessionNumber = cdTransferOutRepository.getSessionNumber();

//            //insertamos en nuestra tabla intermedia
            for (CdTransferOutDto dto : providerExitDtoList) {
                CdTransferOut cdTransferOut = new CdTransferOut();
                cdTransferOut.setSessionNumber(sessionNumber);
                cdTransferOut.setTechKey(contTechKey);
                cdTransferOut.setCarrierName(dto.getCarrierName().isEmpty() ? null : dto.getCarrierName());
                cdTransferOut.setCartonNumber(dto.getCartonNumber().isEmpty() ? null : dto.getCartonNumber());
                cdTransferOut.setPrdLvlNumber(dto.getPrdLvlNumber());
                cdTransferOut.setJdaOrigin(Constants.JDA_ORIGIN_OUT);
                cdTransferOut.setTrfReasonCode(dto.getTrfReasonCode());
                cdTransferOut.setFromLoc(Constants.FROM_LOC);
                cdTransferOut.setToLoc(dto.getToLoc());
                cdTransferOut.setQuantity(dto.getQuantity());
                cdTransferOut.setActionCode(Constants.ACTION_CODE);
                cdTransferOut.setDateCreated(dto.getDateCreated());
                cdTransferOut.setRequestedBy(dto.getRequestedBy());
                cdTransferOut.setInnerPackId(cdTransferOutRepository.getInnerPack(cdTransferOut.getPrdLvlNumber()));
                cdTransferOut.setTrfQtyFlag(Constants.TRF_QTY_FLAG);
                cdTransferOut.setTrfSourceId(Constants.TRF_SOURCE_ID);
                cdTransferOut.setTrfLote(dto.getTrfLote());
                cdTransferOut.setTrfVctoLote(dto.getTrfVctoLote());


//
                cdTransferOutRepository.save(cdTransferOut);
                contTechKey++;
            }
            //consumimos el procedure para insertar los datos en pmm
            cdTransferOutRepository.storeProcedure(sessionNumber);

            //validamos errores y hacemos insert
            Integer validateExitsError = cdTransferOutRepository.validateExitsError(sessionNumber);
//
            if(validateExitsError != 0){
                List<CodErrorSdi> errorSdiList = codErrorSdiRepository.listError(sessionNumber, "CTO");

                for (CodErrorSdi codErrorSdi: errorSdiList) {
                    CdTransferOut cdTransferOut = cdTransferOutRepository.getCdTransferOut(sessionNumber, codErrorSdi.getTechKey());

                    ErrorsDto errorsDtoBody = new ErrorsDto();
                    String identificador = "Codigo Material: " + cdTransferOut.getPrdLvlNumber() + ", Nro Lote: " + cdTransferOut.getTrfLote();
                    errorsDtoBody.setIdentifier(identificador);
                    errorsDtoBody.setMessage(codErrorSdi.getRejDesc());
                    errorsDtoList.add(errorsDtoBody);

                    ErrorsDto errorsDtoLi = new ErrorsDto();
                    errorsDtoLi.setIdentifier(cdTransferOut.getTrfSourceId());
                    errorsDtoLi.setMessage(codErrorSdi.getRejDesc());
                    listResponseBody.add(errorsDtoLi);
                }

                requestBody = mapper.writeValueAsString(providerExitDtoList);
                status = providerExitDtoList.size() == validateExitsError ? "F" : "FP";

                responseDto.setStatus(false);
                responseDto.setMessage(status.equalsIgnoreCase("F")  ? Constants.MESSAGE_FALLO_TOTAL_TRANSACTION : Constants.MESSAGE_FALLO_PARCIAL_TRANSACTION);
                responseDto.setCode(HttpStatus.OK.value());
                responseDto.setBody(listResponseBody);

                responseBody = mapper.writeValueAsString(responseDto);

                TransactionLog tl = transactionLogService.saveTransactionLog(Constants.NAME_TRANSACTION_OUT,
                        "T", "CTO", "Transaccion" ,status , requestBody , responseBody,sessionNumber);

                for(ErrorsDto errorsDto: errorsDtoList){
                    transactionLogErrorService.saveTransactionLogError(tl,errorsDto.getIdentifier(),errorsDto.getMessage());
                }

            } else {
                requestBody = mapper.writeValueAsString(providerExitDtoList);
                status = "C";

                responseDto.setStatus(true);
                responseDto.setMessage(Constants.MESSAGE_OK_TRANSACTION);
                responseDto.setCode(HttpStatus.OK.value());
                responseBody = mapper.writeValueAsString(responseDto);

                transactionLogService.saveTransactionLog(Constants.NAME_TRANSACTION_OUT,
                        "T", "CTO", "Transaccion",status, requestBody, responseBody,sessionNumber);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseDto;
    }
}
