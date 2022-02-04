package com.farmaciasperuanas.pmmli.provider.service;

import com.farmaciasperuanas.pmmli.provider.entity.TransactionLog;

public interface TransactionLogErrorService {
    void saveTransactionLogError(TransactionLog tl, String identifier, String message );
}
