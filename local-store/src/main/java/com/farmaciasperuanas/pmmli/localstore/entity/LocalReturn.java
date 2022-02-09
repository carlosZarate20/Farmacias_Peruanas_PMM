package com.farmaciasperuanas.pmmli.localstore.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "LOCAL_RETURN", schema = "SWLI")
public class LocalReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LOCAL_RETURN")
    private Long idLocalReturn;

    @Column(name = "TECH_KEY")
    private Integer techKey;

    @Column(name = "CARRIER_NAME")
    private String carrierName;

    @Column(name = "CARTON_NUMBER")
    private String cartonNumber;

    @Column(name = "PRD_LVL_NUMBER")
    private String prdLvlNumber;

    @Column(name = "JDA_ORIGIN")
    private String jdaOrigin;

    @Column(name = "TRF_REASON_CODE")
    private String trfReasonCode;

    @Column(name = "FROM_LOC")
    private Integer fromLoc;

    @Column(name = "TO_LOC")
    private Integer toLoc;

    @Column(name = "QUANTITY")
    private Double quantity;
}
