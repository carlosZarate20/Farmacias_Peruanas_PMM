package com.farmaciasperuanas.pmmli.merchandisemovements.service;

import com.farmaciasperuanas.pmmli.merchandisemovements.dto.CdTransferInDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ErrorsDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.CdTransferIn;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.CodErrorSdi;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.TransactionLogError;
import com.farmaciasperuanas.pmmli.merchandisemovements.repository.CdTransferInRepository;
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
public class CdTransferInServiceImpl implements CdTransferInService {
    private static final Gson GSON = new Gson();

    @Autowired
    private CdTransferInRepository cdTransferInRepository;

    @Autowired
    private TransactionLogService transactionLogService;

    @Autowired
    private CodErrorSdiRepository codErrorSdiRepository;

    @Autowired
    private TransactionLogErrorService transactionLogErrorService;

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
        List<ErrorsDto> listResponseBody = new ArrayList<>();
        Integer trfNumber = 0;
        try {
            ObjectMapper mapper = new ObjectMapper();
            //obtenemos el correlativo session_number
            sessionNumber = cdTransferInRepository.getSessionNumber();

            //insertamos en nuestra tabla intermedia
            for (CdTransferInDto dto : providerExitDtoList) {
                CdTransferIn cdTransferIn = new CdTransferIn();
                cdTransferIn.setSessionNumber(sessionNumber);
                cdTransferIn.setTechKey(contTechKey);
                cdTransferIn.setCarrierName(dto.getCarrierName().isEmpty() ? null : dto.getCarrierName());
                cdTransferIn.setCartonNumber(dto.getCartonNumber().isEmpty() ? null : dto.getCartonNumber());

                trfNumber = cdTransferInRepository.getTrfNumber(cdTransferIn.getCartonNumber());
                cdTransferIn.setTrfNumber(trfNumber);

                cdTransferIn.setPrdLvlNumber(dto.getPrdLvlNumber());
                cdTransferIn.setJdaOrigin(Constants.JDA_ORIGIN_IN);
                cdTransferIn.setTrfReasonCode(dto.getTrfReasonCode());
                cdTransferIn.setFromLoc(Constants.FROM_LOC);
                cdTransferIn.setToLoc(dto.getToLoc());
                cdTransferIn.setQuantity(dto.getQuantity());
                cdTransferIn.setActionCode(Constants.ACTION_CODE_IN);
                cdTransferIn.setDateCreated(dto.getDateCreated());
                cdTransferIn.setRequestedBy(dto.getRequestedBy());
                cdTransferIn.setInnerPackId(cdTransferInRepository.getInnerPack(cdTransferIn.getPrdLvlNumber()));
                cdTransferIn.setTrfQtyFlag(Constants.TRF_QTY_FLAG);
                cdTransferIn.setTrfSourceId(Constants.TRF_SOURCE_ID);
                cdTransferIn.setTrfLote(dto.getTrfLote());
                cdTransferIn.setTrfVctoLote(dto.getTrfVctoLote());
                cdTransferIn.setReceiptDate(dto.getReceiptDate());
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
                List<CodErrorSdi> errorSdiList = codErrorSdiRepository.listError(sessionNumber, "CTI");

                for (CodErrorSdi codErrorSdi: errorSdiList) {
                    CdTransferIn cdTransferIn = cdTransferInRepository.getCdTransferIn(sessionNumber, codErrorSdi.getTechKey());
                    ErrorsDto errorsDtoBody = new ErrorsDto();
                    String identificador = "Codigo Material: " + cdTransferIn.getPrdLvlNumber() + ", Nro Lote: " + cdTransferIn.getTrfLote();
                    errorsDtoBody.setIdentifier(identificador);
                    errorsDtoBody.setMessage(codErrorSdi.getRejDesc());
                    errorsDtoList.add(errorsDtoBody);

                    ErrorsDto errorsDtoLi = new ErrorsDto();
                    errorsDtoLi.setIdentifier(cdTransferIn.getPrdLvlNumber());
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

                TransactionLog tl = transactionLogService.saveTransactionLog(Constants.NAME_TRANSACTION_IN,
                        "T", "CTI", "Transaccion" ,status , requestBody , responseBody,sessionNumber);

                for(ErrorsDto errorsDto: errorsDtoList){
                    transactionLogErrorService.saveTransactionLogError(tl,errorsDto.getIdentifier(),errorsDto.getMessage());
                }

            } else {
                requestBody = mapper.writeValueAsString(providerExitDtoList);
                status = "C";

                responseDto.setStatus(true);
                responseDto.setMessage(Constants.MESSAGE_OK_TRANSACTION);
                responseDto.setCode(HttpStatus.OK.value());
                responseDto.setBody(sessionNumber);
                responseBody = mapper.writeValueAsString(responseDto);

                transactionLogService.saveTransactionLog(Constants.NAME_TRANSACTION_IN,
                        "T", "CTI", "Transaccion",status, requestBody, responseBody,sessionNumber);
            }

        } catch (Exception e) {
            responseDto.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatus(false);
            responseDto.setBody(e.getClass());
            responseDto.setMessage(e.getMessage());
            e.printStackTrace();
        }

        return responseDto;
    }
}
