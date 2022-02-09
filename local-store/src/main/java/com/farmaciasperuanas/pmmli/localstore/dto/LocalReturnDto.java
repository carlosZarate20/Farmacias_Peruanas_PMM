package com.farmaciasperuanas.pmmli.localstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocalReturnDto {
    private String carrierName;
    private String cartonNumber;
    private String prdLvlNumber;
    private Integer toLoc;
    private Double quantity;
    private Date dateCreated;
    private String requestedBy;
    private String trfLote;
    private Date trfVctoLote;
}
