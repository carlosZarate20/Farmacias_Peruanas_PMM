package com.farmaciasperuanas.pmmli.localstore.service;

import com.farmaciasperuanas.pmmli.localstore.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.localstore.repository.TransactionLogRepository;
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
    public TransactionLog saveTransactionLog(String nameTransaction, String codeOp, String codeTransaction, String typeTransaction, String status, String requestBody, String responseBody,Integer sessionNumber) {
        Integer cantTransaction = 0;
        String state = "";
        String dateTransaction = "";

        try {
            cantTransaction = transactionLogRepository.getCanTransaction(codeTransaction);

            if(cantTransaction == null){
                cantTransaction = 0;
            }

            cantTransaction += 1;

            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Date date = Calendar.getInstance().getTime();
            dateTransaction = dt.format(date);
            TransactionLog tl = new TransactionLog();
            tl.setCantTransaction(cantTransaction);
            tl.setDateTransaction(date);
            tl.setNameTransaction(nameTransaction);
            tl.setTypeTransaction(typeTransaction);
            tl.setCantTransaction(cantTransaction);
            tl.setJsonRequest(requestBody);
            tl.setJsonResponse(responseBody);
            tl.setState(status);
            tl.setCode(codeOp);
            tl.setCodeTransaction(codeTransaction);
            tl.setSessionNumber(sessionNumber);
            TransactionLog saved = transactionLogRepository.save(tl);

            return saved;
//            transactionLogRepository.saveTransactionLog(nameTransaction, codeOp, codeTransaction,
//                    typeTransaction, cantTransaction, dateTransaction, status,
//                    requestBody, responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
