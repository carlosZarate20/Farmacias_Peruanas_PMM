package com.farmaciasperuanas.pmmli.localstore.service;

import com.farmaciasperuanas.pmmli.localstore.entity.TransactionLog;

public interface TransactionLogService {
    TransactionLog saveTransactionLog(String nameTransaction, String codeOp, String codeTransaction, String typeTransaction,
                                      String status, String requestBody, String responseBody);
}
