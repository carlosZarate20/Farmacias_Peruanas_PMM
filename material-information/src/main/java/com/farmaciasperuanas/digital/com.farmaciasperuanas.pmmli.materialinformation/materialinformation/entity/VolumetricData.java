package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "VOLUMETRIC_DATA", schema = "SWLI")
public class VolumetricData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VOLUMETRIC_DATA")
    private Integer idVolumetricData;

    @Column(name = "MATERIAL_CODE")
    private String materialCode;

    @Column(name = "UMA")
    private String uma;

    @Column(name = "EAN_UPC")
    private String eanUpc;

    @Column(name = "TP")
    private String tp;

    @Column(name = "LENGTH")
    private Double length;

    @Column(name = "WIDTH")
    private Double width;

    @Column(name = "HEIGHT")
    private Double height;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "VOLUME")
    private Double volume;

    @Column(name = "UV")
    private String uv;

    @Column(name = "GROSS_WEIGHT")
    private Double grossWeight;

    @Column(name = "WEIGHT_UNIT")
    private String weightUnit;

    @Column(name = "UPDATED")
    private Long updated;

    @Column(name = "FLAGLI")
    private Long flagLi;
}
