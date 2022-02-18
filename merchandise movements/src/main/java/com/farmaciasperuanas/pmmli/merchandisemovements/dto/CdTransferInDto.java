package com.farmaciasperuanas.pmmli.merchandisemovements.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CdTransferInDto {
    private String carrierName;
    private String cartonNumber;
    private String prdLvlNumber;
    private String trfReasonCode;
    private Integer toLoc;
    private Double quantity;
    private Date dateCreated;
    private Date receiptDate;
    private String requestedBy;
    private String trfLote;
    private Date trfVctoLote;
}
