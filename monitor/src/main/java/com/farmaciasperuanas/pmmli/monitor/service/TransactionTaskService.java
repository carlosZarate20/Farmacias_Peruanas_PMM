package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.entity.TransactionTask;
import com.farmaciasperuanas.pmmli.monitor.repository.TransactionTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionTaskService {

    @Autowired
    TransactionTaskRepository transactionTaskRepository;

    public TransactionTask getTransactionTaskByCode (String code) {
        return  transactionTaskRepository.getTransactionTaskByCodeWork(code);
    }

}
