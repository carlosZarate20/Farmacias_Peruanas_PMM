package com.farmaciasperuanas.pmmli.localstore.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "COD_ERROR_SDI", schema = "SWLI")
public class CodErrorSdi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COD_ERROR_SDI")
    private Long idCodErrorSdi;

    @Column(name = "SESSION_NUMBER")
    private Integer sessionNumber;

    @Column(name = "TECH_KEY")
    private Integer techKey;

    @Column(name = "TYPE_TRANSACTION")
    private String typeTransaction;

    @Column(name = "REJ_CODE")
    private Integer rejCode;

    @Column(name = "REJ_DESC")
    private String rejDesc;
}
