package com.farmaciasperuanas.pmmli.provider.service;

import com.farmaciasperuanas.pmmli.provider.entity.TransactionLog;

public interface TransactionLogService {
    TransactionLog saveTransactionLog(String nameTransaction, String codeOp, String codeTransaction, String typeTransaction,
                                      String status, String requestBody, String responseBody);
}
