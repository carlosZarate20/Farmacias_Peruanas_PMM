package com.farmaciasperuanas.pmmli.localstore.service;

import com.farmaciasperuanas.pmmli.localstore.entity.TransactionLog;
import org.springframework.stereotype.Service;

public interface TransactionLogErrorService {
    void saveTransactionLogError(TransactionLog tl, String identifier, String message );
}
