package com.farmaciasperuanas.pmmli.monitor.dto;

import com.farmaciasperuanas.pmmli.monitor.entity.TransactionLogError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransanctionDetailDto {
    private String id;
    private Integer sessionNumber;
    private String nameTransaction;
    private String state;
    private String dateTransaction;
    private String request;
    private String response;
    private List<TransactionLogError> transactionLogErrors;
}
