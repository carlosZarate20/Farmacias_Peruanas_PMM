package com.farmaciasperuanas.pmmli.localstore.service;

public interface TransactionLogService {
    void saveTransactionLog(String nameTransaction, String codeOp, String codeTransaction, String typeTransaction,
                            String status, String requestBody, String responseBody);
}
