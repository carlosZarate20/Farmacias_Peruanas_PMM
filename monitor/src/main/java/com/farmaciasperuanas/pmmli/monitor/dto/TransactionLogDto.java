package com.farmaciasperuanas.pmmli.monitor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionLogDto {
    private Integer idTran;
    private String identTransaction;
    private String nameTransaction;
    private String estado;
    private String fechaTransaccion;
}
