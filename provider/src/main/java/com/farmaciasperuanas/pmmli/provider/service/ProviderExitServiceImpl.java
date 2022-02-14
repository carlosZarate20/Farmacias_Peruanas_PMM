package com.farmaciasperuanas.pmmli.provider.service;

import com.farmaciasperuanas.pmmli.provider.dto.ErrorsDto;
import com.farmaciasperuanas.pmmli.provider.dto.ProviderExitDto;
import com.farmaciasperuanas.pmmli.provider.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.provider.entity.CodErrorSdi;
import com.farmaciasperuanas.pmmli.provider.entity.ProviderExit;
import com.farmaciasperuanas.pmmli.provider.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.provider.repository.CodErrorSdiRepository;
import com.farmaciasperuanas.pmmli.provider.repository.ProviderExitRepository;
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
    private TransactionLogErrorService transactionLogErrorService;

    @Autowired
    private CodErrorSdiRepository codErrorSdiRepository;

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
        List<ErrorsDto> listResponseBody = new ArrayList<>();
        try{
            ObjectMapper mapper = new ObjectMapper();
            //obtenemos el correlativo session_number
            sessionNumber = providerExitRepository.getSessionNumber();

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
                providerExit.setDateCreated(new Date());
                String prdLvlNumber = providerExitDto.getPrdLvlNumber();
                Integer innerPackId = providerExitRepository.getInnerPackProcedure(prdLvlNumber);
                providerExit.setInnerPackId(innerPackId);
                providerExit.setRtvEntryMethod(Constants.RTV_ENTRY_METHOD);
                providerExit.setRtvLote(providerExitDto.getRtvLote());
                providerExit.setRtvVctoLote(providerExitDto.getRtvVctoLote());

                providerExitRepository.save(providerExit);
                contTechKey++;
            }
            //consumimos el procedure para insertar los datos en pmm
            providerExitRepository.storeProcedure(sessionNumber);

            //validamos errores y hacemos insert
            Integer validateExitsError = providerExitRepository.validateExitsError(sessionNumber);

            if(validateExitsError != 0){

                List<CodErrorSdi> errorSdiList = codErrorSdiRepository.listError(sessionNumber, "PE");
                for (CodErrorSdi codErrorSdi: errorSdiList) {

                    ProviderExit providerExit = providerExitRepository.getProviderExit(sessionNumber, codErrorSdi.getTechKey());

                    ErrorsDto errorsDtoBody = new ErrorsDto();
                    String identificador = "Codigo Material: " + providerExit.getPrdLvlNumber() + ", Nro Lote: " + providerExit.getRtvLote();
                    errorsDtoBody.setIdentifier(identificador);
                    errorsDtoBody.setMessage(codErrorSdi.getRejDesc());
                    errorsDtoList.add(errorsDtoBody);

                    ErrorsDto errorsDtoLi = new ErrorsDto();
                    errorsDtoLi.setIdentifier(providerExit.getRtvPriorId());
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

                TransactionLog tl = transactionLogService.saveTransactionLog(Constants.NAME_TRANSACTION,
                        "T", "PE", "Transaccion" ,status , requestBody , responseBody, sessionNumber);

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

                transactionLogService.saveTransactionLog(Constants.NAME_TRANSACTION,
                        "T", "PE", "Transaccion",status, requestBody, responseBody, sessionNumber);
            }
        } catch(Exception e){
            responseDto.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatus(false);
            responseDto.setBody(e.getClass());
            responseDto.setMessage(e.getMessage());
            e.printStackTrace();
        }

        return responseDto;
    }
}
