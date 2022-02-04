package com.farmaciasperuanas.pmmli.materialinformation.service;

import com.farmaciasperuanas.pmmli.materialinformation.entity.TransactionLog;

public interface TransactionLogErrorService {
    void saveTransactionLogError(TransactionLog tl, String identifier, String message );
}
