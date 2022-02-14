package com.farmaciasperuanas.pmmli.merchandisemovements.service;

import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ErrorsDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.InventoryAdjustmentDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.MerchandiseTransferDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.CodErrorSdi;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.InventoryAdjustment;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.MerchandiseTransfer;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.merchandisemovements.repository.CodErrorSdiRepository;
import com.farmaciasperuanas.pmmli.merchandisemovements.repository.MerchandiseTransferRepository;
import com.farmaciasperuanas.pmmli.merchandisemovements.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MerchandiseTransferServiceImpl implements MerchandiseTransferService {

    @Autowired
    private MerchandiseTransferRepository merchandiseTransferRepository;

    @Autowired
    private TransactionLogService transactionLogService;

    @Autowired
    private CodErrorSdiRepository codErrorSdiRepository;

    @Autowired
    private TransactionLogErrorService transactionLogErrorService;

    @Override
    public ResponseDto enviarMerchandiseTransfer(List<MerchandiseTransferDto> merchandiseTransferDtoList) {
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
            transSession = merchandiseTransferRepository.getTransSession();

            //insertamos en nuestra tabla intermedia
            for(MerchandiseTransferDto merchandiseTransferDto: merchandiseTransferDtoList){
                MerchandiseTransfer merchandiseTransfer = new MerchandiseTransfer();
                merchandiseTransfer.setTransSession(transSession);
                merchandiseTransfer.setTransUser(merchandiseTransferDto.getTransUser());
                merchandiseTransfer.setTransBatchDate(new Date());
                merchandiseTransfer.setTransSource(Constants.TRANS_SOURCE_MF);
                merchandiseTransfer.setTransAudited(Constants.TRANS_AUDITED);
                merchandiseTransfer.setTransSequence(contTransSequence);
                merchandiseTransfer.setTransTrnCode(merchandiseTransferDto.getTransTrnCode());
                merchandiseTransfer.setTransTypeCode(merchandiseTransferDto.getTransTypeCode());
                String dateFormat = dt.format(merchandiseTransferDto.getTransDate());
                Date dateTrans = dt.parse(dateFormat);
                merchandiseTransfer.setTransDate(dateTrans);
                merchandiseTransfer.setInvMrptCode(merchandiseTransferDto.getInvMrptCode());
                merchandiseTransfer.setInvDrptCode(merchandiseTransferDto.getInvDrptCode());
                merchandiseTransfer.setTransCurrCode(Constants.TRANS_CURR_CODE);
                merchandiseTransfer.setTransOrgLvlNumber(Constants.TRANS_ORG_LVL_NUMBER);
                merchandiseTransfer.setTransPrdLvlNumber(merchandiseTransferDto.getTransPrdLvlNumber());
                merchandiseTransfer.setProcSource(merchandiseTransferDto.getProcSource());
                merchandiseTransfer.setTransQty(merchandiseTransferDto.getTransQty());
                merchandiseTransfer.setInnerPackId(merchandiseTransferRepository.getInnerPack(merchandiseTransfer.getTransPrdLvlNumber()));
                merchandiseTransfer.setTransInners(merchandiseTransferDto.getTransInners());
                merchandiseTransfer.setTransLote(merchandiseTransferDto.getTransLote());
                merchandiseTransfer.setTransVctoLote(merchandiseTransferDto.getTransVctoLote());

                merchandiseTransferRepository.save(merchandiseTransfer);

                contTransSequence++;
            }

            //consumimos el procedure para insertar los datos en pmm
            merchandiseTransferRepository.storeProcedure(transSession);

            //validamos errores y hacemos insert
            Integer validateExitsError = merchandiseTransferRepository.validateExitsError(transSession);
            if(validateExitsError != 0){
                List<CodErrorSdi> errorSdiList = codErrorSdiRepository.listError(transSession, "MT");
                for (CodErrorSdi codErrorSdi: errorSdiList) {
                    MerchandiseTransfer merchandiseTransfer = merchandiseTransferRepository.getMerchandiseTransfer(transSession, codErrorSdi.getTechKey());

                    ErrorsDto errorsDtoBody = new ErrorsDto();
                    String identificador = "Codigo Material: " + merchandiseTransfer.getTransPrdLvlNumber() + ", Nro Lote: " + merchandiseTransfer.getTransLote();
                    errorsDtoBody.setIdentifier(identificador);
                    errorsDtoBody.setMessage(codErrorSdi.getRejDesc());
                    errorsDtoList.add(errorsDtoBody);

                    ErrorsDto errorsDtoLi = new ErrorsDto();
                    errorsDtoLi.setIdentifier(merchandiseTransfer.getTransSource());
                    errorsDtoLi.setMessage(codErrorSdi.getRejDesc());
                    listResponseBody.add(errorsDtoLi);
                }
                requestBody = mapper.writeValueAsString(errorSdiList);
                status = merchandiseTransferDtoList.size() == validateExitsError ? "F" : "FP";

                responseDto.setStatus(false);
                responseDto.setMessage(status.equalsIgnoreCase("F")  ? Constants.MESSAGE_FALLO_TOTAL_TRANSACTION : Constants.MESSAGE_FALLO_PARCIAL_TRANSACTION);
                responseDto.setCode(HttpStatus.OK.value());
                responseDto.setBody(listResponseBody);

                responseBody = mapper.writeValueAsString(responseDto);

                TransactionLog tl = transactionLogService.saveTransactionLog(Constants.NAME_MERCHANDISE_TRANSFER,
                        "T", "MT", "Transaccion" ,status , requestBody , responseBody,transSession);

                for(ErrorsDto errorsDto: errorsDtoList){
                    transactionLogErrorService.saveTransactionLogError(tl,errorsDto.getIdentifier(),errorsDto.getMessage());
                }
            }else {
                requestBody = mapper.writeValueAsString(merchandiseTransferDtoList);
                status = "C";

                responseDto.setStatus(true);
                responseDto.setMessage(Constants.MESSAGE_OK_TRANSACTION);
                responseDto.setCode(HttpStatus.OK.value());
                responseBody = mapper.writeValueAsString(responseDto);

                transactionLogService.saveTransactionLog(Constants.NAME_MERCHANDISE_TRANSFER,
                        "T", "MT", "Transaccion",status, requestBody, responseBody,transSession);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return responseDto;
    }
}
