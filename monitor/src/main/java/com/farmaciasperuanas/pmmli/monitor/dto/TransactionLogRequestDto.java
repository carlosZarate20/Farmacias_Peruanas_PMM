package com.farmaciasperuanas.pmmli.monitor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionLogRequestDto {

    private Date startDate;
    private Date endDate;
    private String typeTransaction;
    private String state;
    private Integer page;
    private Integer rows;
}
