package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.dto.DataMaestraDto;
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
            dataMaestraDto.setCantTransacciones(Integer.parseInt(String.valueOf(object[1])));
            dataMaestraDto.setUltimaEjecucion(dt.format(object[2]));
            dataMaestraDto.setCodeTransaction(String.valueOf(object[3]));
            dataMaestraDto.setTypeTransaction(String.valueOf(object[4]));

            dataMaestraDtoList.add(dataMaestraDto);
        }
        return dataMaestraDtoList;
    }
}
