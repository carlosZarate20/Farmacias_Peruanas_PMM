package com.farmaciasperuanas.pmmli.monitor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CantMaestroDto {
    private Integer cantMaestroCorrecto;
    private Integer cantMaestroFallido;
    private Integer cantTransactionCorrecto;
    private Integer cantTransactionFallido;
    private Integer cantMaestros;
    private Integer cantTransacciones;
    private Integer total;
}