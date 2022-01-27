package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.dto.DataMaestraDto;
import com.farmaciasperuanas.pmmli.monitor.dto.TransactionDto;
import com.farmaciasperuanas.pmmli.monitor.dto.TransanctionDetailDto;
import com.farmaciasperuanas.pmmli.monitor.repository.TransactionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
                dataMaestraDto.setUltimaEjecucion(" ");
            }

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
}
