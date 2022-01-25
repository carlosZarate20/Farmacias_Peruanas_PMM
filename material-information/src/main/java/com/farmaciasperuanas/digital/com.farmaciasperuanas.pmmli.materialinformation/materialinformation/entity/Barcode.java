package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "BARCODE", schema = "SWLI")
public class Barcode implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_BARCODE")
    private Integer idBarcode;

    @Column(name = "MATERIAL_CODE")
    private String materialCode;

    @Column(name = "UM")
    private String um;

    @Column(name = "EAN_UPC")
    private String eanUPC;

    @Column(name = "TP")
    private String tp;

    @Column(name = "J_EAN")
    private String jEan;

    @Column(name = "UPDATED")
    private Long updated;

    @Column(name = "FLAGLI")
    private Long flagLi;
}
