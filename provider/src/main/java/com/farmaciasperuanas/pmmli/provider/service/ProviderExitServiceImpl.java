package com.farmaciasperuanas.pmmli.provider.service;

import com.farmaciasperuanas.pmmli.provider.dto.ErrorsDto;
import com.farmaciasperuanas.pmmli.provider.dto.ProviderExitDto;
import com.farmaciasperuanas.pmmli.provider.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.provider.entity.ProviderExit;
import com.farmaciasperuanas.pmmli.provider.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.provider.entity.TransactionLogError;
import com.farmaciasperuanas.pmmli.provider.repository.ProviderExitRepository;
import com.farmaciasperuanas.pmmli.provider.repository.TransactionLogErrorRepository;
import com.farmaciasperuanas.pmmli.provider.repository.TransactionLogRepository;
import com.farmaciasperuanas.pmmli.provider.util.Constants;
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
public class ProviderExitServiceImpl implements ProviderExitService{

    private static final Gson GSON = new Gson();

    @Autowired
    private ProviderExitRepository providerExitRepository;

    @Autowired
    private TransactionLogService transactionLogService;

    @Autowired
    private TransactionLogErrorRepository transactionLogErrorRepository;

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Override
    public ResponseDto guardarSalidaProveedorPMM(List<ProviderExitDto> providerExitDtoList) {
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
        try{
            ObjectMapper mapper = new ObjectMapper();
            //obtenemos el correlativo session_number
            sessionNumber = providerExitRepository.getSessioNumber();

            //insertamos en nuestra tabla intermedia
            for(ProviderExitDto providerExitDto: providerExitDtoList){
                ProviderExit providerExit = new ProviderExit();
                providerExit.setSessionNumber(sessionNumber);
                providerExit.setTechKey(contTechKey);
                providerExit.setPrdLvlNumber(providerExitDto.getPrdLvlNumber());
                providerExit.setRtvShipLoc(Constants.RTV_SHIP_LOC);
                providerExit.setCarrierName(Constants.CARRIER_NAME);
                providerExit.setRtvCartonId(providerExitDto.getRtvCartonId());
                providerExit.setJdaOrigin(Constants.JDA_ORIGIN);
                providerExit.setVendorNumber(providerExitDto.getVendorNumber());
                providerExit.setRtvTypeId(Constants.RTV_TYPE_ID);
                providerExit.setRequestedBy(providerExitDto.getRequestedBy());
                providerExit.setActionCode(Constants.ACTION_CODE);
                providerExit.setRtvPriorId(providerExitDto.getRtvPriorId());
                providerExit.setQuantity((double) providerExitDto.getQuantity());
                providerExit.setDateCreated(dateCreate);
                providerExit.setInnerPackId(providerExitRepository.getInnerPack(providerExit.getPrdLvlNumber()));
                providerExit.setRtvEntryMethod(Constants.RTV_ENTRY_METHOD);
                providerExit.setRtvLote(providerExitDto.getRtvLote());
                providerExit.setRtvVctoLote(providerExitDto.getRtvVctoLote());
                providerExit.setFlagLi(0L);

                providerExitRepository.save(providerExit);
                contTechKey++;
            }
            //consumimos el procedure para insertar los datos en pmm
            providerExitRepository.storeProcedure(sessionNumber);

            //validamos errores y hacemos insert
            Integer validateExitsError = providerExitRepository.validateExitsError(sessionNumber);

            if(validateExitsError != 0){
                requestBody = mapper.writeValueAsString(providerExitDtoList);
                status = providerExitDtoList.size() == validateExitsError ? "F" : "FP";

                responseDto.setStatus(false);
                responseDto.setMessage(status.equalsIgnoreCase("F")  ? Constants.MESSAGE_FALLO_TOTAL_TRANSACTION : Constants.MESSAGE_FALLO_PARCIAL_TRANSACTION);
                responseDto.setCode(HttpStatus.OK.value());

                responseBody = mapper.writeValueAsString(responseDto);

                TransactionLog tl = transactionLogService.saveTransactionLog(Constants.NAME_TRANSACTION,
                        "T", "PE", "Transaccion" ,status , requestBody , responseBody);

                providerExitRepository.storeProcedureLogError(tl.getIdTransacctionLog(),
                        sessionNumber, "Código del material: ",
                        ", Nro Lote: ",
                        "PE");

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

                transactionLogService.saveTransactionLog(Constants.NAME_TRANSACTION,
                        "T", "PE", "Transaccion",status, requestBody, responseBody);
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return responseDto;
    }
}
