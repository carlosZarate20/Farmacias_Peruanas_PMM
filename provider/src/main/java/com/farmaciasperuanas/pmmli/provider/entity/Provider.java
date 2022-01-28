package com.farmaciasperuanas.pmmli.provider.entity;

import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "PROVIDER")
public class Provider{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROVIDER")
    private Integer idProvider;

    @Column(name = "CODE")
    private String code;

    @Column(name = "BUSINESS_NAME")
    private String businessName;

    @Column(name = "REGION")
    private String region;

    @Column(name = "STREET")
    private String street;

    @Column(name = "SEARCH")
    private String search;

    @Column(name = "RAMO")
    private String ramo;

    @Column(name = "RUC")
    private String ruc;

    @Column(name = "NIF2")
    private String nif2;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "UPDATED")
    private Long updated;

    @Column(name = "FLAGLI")
    private Long flagLi;
}
