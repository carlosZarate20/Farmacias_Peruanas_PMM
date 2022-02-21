package com.farmaciasperuanas.pmmli.provider.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderExitDto {
    private String prdLvlNumber;
    private String rtvCartonId;
    private String vendorNumber;
    private String requestedBy;
    private String rtvPriorId;
    private Integer quantity;
    private String reference;
    private String rtvDtlNotes;
    private String rtvLote;
    private Date rtvVctoLote;
}
