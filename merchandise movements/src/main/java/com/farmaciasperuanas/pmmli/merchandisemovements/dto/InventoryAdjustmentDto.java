package com.farmaciasperuanas.pmmli.merchandisemovements.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryAdjustmentDto {

    private String transUser;
    private String transSource;
    private String transTrnCode;
    private String transTypeCode;
    private Date transDate;
    private String invMrptCode;
    private String invDrptCode;
    private String transPrdLvlNumber;
    private String procSource;
    private Integer transQty;
    private Integer transInners;
    private String transLote;
    private Date transVctoLote;
}
