package com.farmaciasperuanas.pmmli.materialinformation.service;

public interface TransactionLogService {
    void saveTransactionLog(String nameTransaction, String codeOp, String codeTransaction, String typeTransaction,
                            String status, String requestBody, String responseBody);
}
