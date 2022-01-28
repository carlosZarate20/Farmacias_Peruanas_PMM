package com.farmaciasperuanas.pmmli.provider.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "MATERIAL_PROVIDER", schema = "SWLI")
public class MaterialProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MATERIAL_PROVIDER")
    private Integer idMaterialProvider;

    @Column(name = "MATERIAL_INKA")
    private String materialInka;

    @Column(name = "MATERIAL_MIFA")
    private String materialMifa;

    @Column(name = "PROVIDER_CODE")
    private String providerCode;

    @Column(name = "LAB_CODE")
    private String labCode;

    @Column(name = "PRINCIPAL_PROVIDER")
    private String principalProvider;

    @Column(name = "UPDATED")
    private Long updated;

    @Column(name = "FLAGLI")
    private Long flagLi;
}
