package com.farmaciasperuanas.pmmli.monitor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMasterTransactionDto {
    private boolean status;
    private int code;
    private ResponseApi body;
    private String message;
}
