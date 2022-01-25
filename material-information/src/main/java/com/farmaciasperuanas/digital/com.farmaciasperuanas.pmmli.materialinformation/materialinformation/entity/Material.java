package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "MATERIAL", schema = "SWLI")
public class Material implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MATERIAL")
    private Integer idMaterial;

    @Column(name = "INKA")
    private String inka;

    @Column(name = "MIFA")
    private String mifa;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ARTICLE_GROUP")
    private String articleGroup;

    @Column(name = "ARTICLE_GROUP_DESCRIPTION")
    private String articleGroupDescription;

    @Column(name = "UNIT_PRICE")
    private Double unitPrice;

    @Column(name = "CONTROL")
    private String control;

    @Column(name = "ASSORTMENT")
    private String assortment;

    @Column(name = "INDUCTION_ZONE")
    private String inductionZone;

    @Column(name = "FAMILY")
    private String family;

    @Column(name = "FAMILY_CODE")
    private String familyCode;

    @Column(name = "COOLED")
    private Integer cooled;

    @Column(name = "INACTIVE")
    private Integer inactive;

    @Column(name = "UMB")
    private String umb;

    @Column(name = "DEMON")
    private String demon;

    @Column(name = "UPDATED")
    private Long updated;

    @Column(name = "FLAGLI")
    private Long flagLi;

}
