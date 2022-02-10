package com.farmaciasperuanas.pmmli.merchandisemovements.service;

import com.farmaciasperuanas.pmmli.merchandisemovements.entity.TransactionLog;

public interface TransactionLogErrorService {
    void saveTransactionLogError(TransactionLog tl, String identifier, String message );
}
