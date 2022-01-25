package com.farmaciasperuanas.pmmli.monitor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataMaestraDto {

    private String nameTransaccion;
    private Integer cantTransacciones;
    private String ultimaEjecucion;
    private String codeTransaction;
    private String typeTransaction;

}
