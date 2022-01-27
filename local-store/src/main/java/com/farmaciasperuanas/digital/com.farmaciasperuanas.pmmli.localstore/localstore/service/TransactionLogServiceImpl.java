package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.localstore.localstore.service;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.localstore.localstore.repository.TransactionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class TransactionLogServiceImpl implements TransactionLogService {

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Override
    public void saveTransactionLog(String nameTransaction, String codeOp, String codeTransaction, String typeTransaction, boolean status, String requestBody, String responseBody) {
        Integer cantTransaction = 0;
        String state = "";
        String dateTransaction = "";

        try {
            if (status) {
                state = "Correcto";
            } else {
                state = "Fallido";
            }

            cantTransaction = transactionLogRepository.getCanTransaction(codeTransaction);

            if(cantTransaction == null){
                cantTransaction = 0;
            }

            cantTransaction += 1;

            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Date date = Calendar.getInstance().getTime();
            dateTransaction = dt.format(date);

            transactionLogRepository.saveTransactionLog(nameTransaction, codeOp, codeTransaction,
                    typeTransaction, cantTransaction, dateTransaction, state,
                    requestBody, responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
