package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.localstore.localstore.entity;

import lombok.Data;

import javax.persistence.*;
import java.awt.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "STORE", schema = "SWLI")
public class Store implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_STORE")
    private Integer idStore;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STREET")
    private String street;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "DEPARTMENT_CODE")
    private String deparmetCode;

    @Column(name = "PROVINCE_CODE")
    private String provinceCode;

    @Column(name = "DISTRICT_CODE")
    private String districtCode;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "COD_FARMA")
    private String codFarma;

    @Column(name = "LOCAL_TYPE")
    private String localType;

    @Column(name = "SOCIETY")
    private String society;

    @Column(name = "UPDATED")
    private Long updated;

    @Column(name = "FLAGLI")
    private Long flagLi;
}
