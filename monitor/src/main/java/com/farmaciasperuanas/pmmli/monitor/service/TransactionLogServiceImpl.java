package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.dto.*;
import com.farmaciasperuanas.pmmli.monitor.entity.ErrorType;
import com.farmaciasperuanas.pmmli.monitor.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.monitor.entity.TransactionType;
import com.farmaciasperuanas.pmmli.monitor.repository.ErrorTypeRepository;
import com.farmaciasperuanas.pmmli.monitor.repository.TransactionLogRepository;
import com.farmaciasperuanas.pmmli.monitor.repository.TransactionTypeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionLogServiceImpl implements TransactionLogService{

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Autowired
    private ErrorTypeRepository errorTypeRepository;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Override
    public List<DataMaestraDto> getDatosMaestros() {
        List<Object[]> listDataMaestra = new ArrayList<>();
        List<DataMaestraDto> dataMaestraDtoList = new ArrayList<>();
        listDataMaestra = transactionLogRepository.getDataMaestra();
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa");
        for(Object[] object: listDataMaestra)
        {
            DataMaestraDto dataMaestraDto = new DataMaestraDto();
            dataMaestraDto.setNameTransaccion(String.valueOf(object[0]));
            dataMaestraDto.setTypeTransaction(String.valueOf(object[1]));
            dataMaestraDto.setTaskState(String.valueOf(object[2]));
            dataMaestraDto.setCodeTransaction(String.valueOf(object[3]));
            dataMaestraDto.setCantTransacciones(Integer.parseInt(String.valueOf(object[4])));
            if(object[5] != null){
                dataMaestraDto.setUltimaEjecucion(dt.format(object[5]));
            }else {
                dataMaestraDto.setUltimaEjecucion("");
            }
            dataMaestraDto.setCronExpression( object[6] == null ? null : String.valueOf(object[6]));
            dataMaestraDtoList.add(dataMaestraDto);
        }
        return dataMaestraDtoList;
    }

    @Override
    public List<TransactionLogDto> listarTransactionDashboard() {

        List<Object[]> listTransaction = new ArrayList<>();
        List<TransactionLogDto> transactionDtoList = new ArrayList<>();

        listTransaction = transactionLogRepository.getDahsboardTransaction();

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa");

        for(Object[] object: listTransaction){
            TransactionLogDto transactionDto = new TransactionLogDto();

            transactionDto.setIdTran(Long.valueOf(String.valueOf(object[0])));
            transactionDto.setIdentTransaction(String.format("%6s", String.valueOf(object[0])).replace(' ','0'));
            transactionDto.setNameTransaction(String.valueOf(object[1]));
            transactionDto.setEstado(String.valueOf(object[2]));
            transactionDto.setFechaTransaccion(dt.format(object[3]));

            transactionDtoList.add(transactionDto);
        }
        return transactionDtoList;
    }

    @Override
    public TransanctionDetailDto getDetailTransaction(Long id) {

        List<Object[]> listDetailTransaction = new ArrayList<>();
//        listDetailTransaction = transactionLogRepository.getTransactionDetail(id);

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa");

        TransanctionDetailDto transanctionDetailDto = new TransanctionDetailDto();
        Optional<TransactionLog> tl =transactionLogRepository.findById(id);
//        for(Object[] object: listDetailTransaction){
//
//            transanctionDetailDto.setNameTransaction(String.valueOf(object[0]));
//            transanctionDetailDto.setState(String.valueOf(object[1]));
//            transanctionDetailDto.setDateTransaction(dt.format(object[2]));
//            transanctionDetailDto.setRequest(String.valueOf(object[3]));
//            transanctionDetailDto.setResponse(String.valueOf(object[4]));
//
//        }
        if(tl.isPresent()){
            TransactionLog tran = tl.get();
            transanctionDetailDto.setRequest(tran.getJsonRequest());
            transanctionDetailDto.setResponse(tran.getJsonResponse());
            transanctionDetailDto.setNameTransaction(tran.getNameTransaction());
            transanctionDetailDto.setState(tran.getState());
            transanctionDetailDto.setDateTransaction(dt.format(tran.getDateTransaction()));
            transanctionDetailDto.setTransactionLogErrors(tran.getTransactionLogErrors());
        }
        return transanctionDetailDto;
    }

    @Override
    public CantMaestroDto getCantidadDatosMonth() {

        List<Object[]> listObject = new ArrayList<>();
        CantMaestroDto cantMaestroDto = new CantMaestroDto();

        LocalDate localDate = LocalDate.now();

        Integer month = localDate.getMonthValue();
        Integer year = localDate.getYear();

        listObject = transactionLogRepository.getCantTransactionMonth(String.valueOf(year), String.valueOf(month));


        for(Object[] objects: listObject){
            cantMaestroDto.setCantMaestroFallido(Integer.parseInt(String.valueOf(objects[0])));
            cantMaestroDto.setCantMaestroCorrecto(Integer.parseInt(String.valueOf(objects[1])));
            cantMaestroDto.setCantTransactionFallido(Integer.parseInt(String.valueOf(objects[2])));
            cantMaestroDto.setCantTransactionCorrecto(Integer.parseInt(String.valueOf(objects[3])));
            cantMaestroDto.setCantMaestros(Integer.parseInt(String.valueOf(objects[4])));
            cantMaestroDto.setCantTransacciones(Integer.parseInt(String.valueOf(objects[5])));
            cantMaestroDto.setTotal(Integer.parseInt(String.valueOf(objects[6])));
        }
        return cantMaestroDto;
    }

    @Override
    public List<TransactionDto> getNameTransaction() {
        List<TransactionDto> transactionDtoList = new ArrayList<>();

        List<TransactionType> transactionTypeList = transactionTypeRepository.getTransactionType();

        for(TransactionType transactionType : transactionTypeList){
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setType(transactionType.getTransactionType());
            transactionDto.setDescription(transactionType.getNameTransaction());

            transactionDtoList.add(transactionDto);
        }
        return transactionDtoList;
    }

    @Override
    public List<ErrorTypeDto> getListError() {
        List<ErrorTypeDto> errorTypeDtoList = new ArrayList<>();

        List<ErrorType> errorTypeList = errorTypeRepository.getErrorType();

        for(ErrorType errorType: errorTypeList){
            ErrorTypeDto errorTypeDto = new ErrorTypeDto();
            errorTypeDto.setTypeError(errorType.getTypeError());
            errorTypeDto.setDescription(errorType.getNameDescription());

            errorTypeDtoList.add(errorTypeDto);
        }
        return errorTypeDtoList;
    }

    @Override
    public DataTableDto<TransactionLogDto> listarTransactionLog(TransactionLogRequestDto transactionLogRequestDto) {

        DataTableDto<TransactionLogDto> response = new DataTableDto<>();
        Integer validateDate = -1;
        Integer validateTypeTransaction= -1;
        Integer validateState = -1;
        String dateInit = "";
        String dateEnd = "";
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dt2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa");
        Double cantpages2 = 0.0;
        Integer cantPages = 0;

        if(!transactionLogRequestDto.getState().equals("") && transactionLogRequestDto.getState() != null){
            validateState = 1;
        }

        if(!transactionLogRequestDto.getTypeTransaction().equals("")  && transactionLogRequestDto.getTypeTransaction() != null){
            validateTypeTransaction = 1;
        }

        if(transactionLogRequestDto.getStartDate() != null && transactionLogRequestDto.getEndDate() != null){
            dateInit = dt.format(transactionLogRequestDto.getStartDate());
            dateEnd = dt.format(transactionLogRequestDto.getEndDate());
            validateDate = 1;
        } else {
            dateInit = "";
            dateEnd = "";
        }

        Integer init = transactionLogRequestDto.getRows() * transactionLogRequestDto.getPage();

        OffsetLimitRequest offsetLimitRequest = new OffsetLimitRequest(init, transactionLogRequestDto.getRows());

        List<TransactionLogDto> transactionLogDtoList = new ArrayList<>();

        List<TransactionLog> transactionLogList =
                transactionLogRepository.getTransactionLogFilter(dateInit,
                dateEnd, transactionLogRequestDto.getState(),
                transactionLogRequestDto.getTypeTransaction(), validateDate,
                validateTypeTransaction, validateState, offsetLimitRequest);

        Integer count = transactionLogRepository.getTransactionLogFilterCount(dateInit,
                dateEnd, transactionLogRequestDto.getState(), transactionLogRequestDto.getTypeTransaction(), validateDate, validateTypeTransaction, validateState);

        if(transactionLogRequestDto.getRows() != transactionLogList.size()){
            cantPages = transactionLogRequestDto.getPage() + 1;
        } else{
            cantpages2 = transactionLogList.size() == 0 ? 0.0 : (double) count / transactionLogList.size();
            cantPages = (int) Math.ceil(cantpages2);
        }

        for(TransactionLog transactionLog: transactionLogList){
            TransactionLogDto transactionLogDto = new TransactionLogDto();
            transactionLogDto.setIdTran(transactionLog.getIdTransacctionLog());
            transactionLogDto.setIdentTransaction(StringUtils.leftPad("" + transactionLog.getIdTransacctionLog(), 6, "0"));
            transactionLogDto.setNameTransaction(transactionLog.getNameTransaction());
            transactionLogDto.setEstado(transactionLog.getState());
            transactionLogDto.setFechaTransaccion(dt2.format(transactionLog.getDateTransaction()));
            transactionLogDtoList.add(transactionLogDto);
        }
        response.setPages(cantPages);
        response.setData(transactionLogDtoList);

//        List<Object[]> transactionLogFilterDtoList = transactionLogRepository.getTransactionLogFilter("27/01/2022",
//                "29/01/2022", "F", "MP", -1, -1, 1, offsetLimitRequest);

        return response;
    }

    private Date getCurrentDate() {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.now();
        localDate.getYear();
        localDate.getMonthValue();

        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
    }


}
