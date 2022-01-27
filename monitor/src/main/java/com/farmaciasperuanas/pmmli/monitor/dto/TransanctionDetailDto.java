package com.farmaciasperuanas.pmmli.monitor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransanctionDetailDto {
    private String nameTransaction;
    private String state;
    private String dateTransaction;
    private String request;
    private String response;
}
