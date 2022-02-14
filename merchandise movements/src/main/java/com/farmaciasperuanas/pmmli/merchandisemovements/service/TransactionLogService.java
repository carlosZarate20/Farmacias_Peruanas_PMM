package com.farmaciasperuanas.pmmli.merchandisemovements.service;


import com.farmaciasperuanas.pmmli.merchandisemovements.entity.TransactionLog;

public interface TransactionLogService {
    TransactionLog saveTransactionLog(String nameTransaction, String codeOp, String codeTransaction, String typeTransaction,
                                      String status, String requestBody, String responseBod, Integer sessionNumber);
}
