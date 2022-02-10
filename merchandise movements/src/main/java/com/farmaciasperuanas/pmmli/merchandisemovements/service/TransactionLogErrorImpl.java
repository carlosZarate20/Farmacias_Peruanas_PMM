package com.farmaciasperuanas.pmmli.merchandisemovements.service;

import com.farmaciasperuanas.pmmli.merchandisemovements.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.merchandisemovements.entity.TransactionLogError;
import com.farmaciasperuanas.pmmli.merchandisemovements.repository.TransactionLogErrorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionLogErrorImpl implements TransactionLogErrorService {

    @Autowired
    TransactionLogErrorRepository transactionLogErrorRepository;

    @Override
    public void saveTransactionLogError(TransactionLog tl, String identifier, String message) {
        TransactionLogError tlr = new TransactionLogError();
        tlr.setTransactionLog(tl);
        tlr.setIdentifier(identifier);
        tlr.setMessage(message);
        transactionLogErrorRepository.save(tlr);
    }
}
