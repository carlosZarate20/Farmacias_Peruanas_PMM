package com.farmaciasperuanas.pmmli.materialinformation.service;

import com.farmaciasperuanas.pmmli.materialinformation.entity.TransactionLog;

public interface TransactionLogService {
    TransactionLog saveTransactionLog(String nameTransaction, String codeOp, String codeTransaction, String typeTransaction,
                                      String status, String requestBody, String responseBody, Integer sessionNumber);
}
