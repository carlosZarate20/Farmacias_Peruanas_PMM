package com.farmaciasperuanas.pmmli.materialinformation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BarcodeDto {
    private String eanUPC;
    private String jEan;
    private String material;
    private String tp;
    private String um;
}
