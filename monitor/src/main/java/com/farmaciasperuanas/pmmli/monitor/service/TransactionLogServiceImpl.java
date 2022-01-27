package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.dto.CantMaestroDto;
import com.farmaciasperuanas.pmmli.monitor.dto.DataMaestraDto;
import com.farmaciasperuanas.pmmli.monitor.dto.TransactionDto;
import com.farmaciasperuanas.pmmli.monitor.dto.TransanctionDetailDto;
import com.farmaciasperuanas.pmmli.monitor.repository.TransactionLogRepository;
import oracle.sql.DATE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TransactionLogServiceImpl implements TransactionLogService{

    @Autowired
    private TransactionLogRepository transactionLogRepository;

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
    public List<TransactionDto> listarTransactionDashboard() {

        List<Object[]> listTransaction = new ArrayList<>();
        List<TransactionDto> transactionDtoList = new ArrayList<>();

        listTransaction = transactionLogRepository.getDahsboardTransaction();

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa");

        for(Object[] object: listTransaction){
            TransactionDto transactionDto = new TransactionDto();

            transactionDto.setIdTran(Integer.parseInt(String.valueOf(object[0])));
            transactionDto.setIdentTransaction(String.format("%6s", String.valueOf(object[0])).replace(' ','0'));
            transactionDto.setNameTransaction(String.valueOf(object[1]));
            transactionDto.setEstado(String.valueOf(object[2]));
            transactionDto.setFechaTransaccion(dt.format(object[3]));

            transactionDtoList.add(transactionDto);
        }
        return transactionDtoList;
    }

    @Override
    public TransanctionDetailDto getDetailTransaction(Integer id) {

        List<Object[]> listDetailTransaction = new ArrayList<>();
        listDetailTransaction = transactionLogRepository.getTransactionDetail(id);

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa");

        TransanctionDetailDto transanctionDetailDto = new TransanctionDetailDto();

        for(Object[] object: listDetailTransaction){

            transanctionDetailDto.setNameTransaction(String.valueOf(object[0]));
            transanctionDetailDto.setState(String.valueOf(object[1]));
            transanctionDetailDto.setDateTransaction(dt.format(object[2]));
            transanctionDetailDto.setRequest(String.valueOf(object[3]));
            transanctionDetailDto.setResponse(String.valueOf(object[4]));
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

    private Date getCurrentDate() {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.now();
        localDate.getYear();
        localDate.getMonthValue();

        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
    }
}
