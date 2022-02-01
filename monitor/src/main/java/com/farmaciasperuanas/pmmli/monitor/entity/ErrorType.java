package com.farmaciasperuanas.pmmli.monitor.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ERROR_TYPE", schema = "SWLI")
public class ErrorType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ERROR_TYPE")
    private Integer idErrorType;

    @Column(name = "TYPE_ERROR")
    private String typeError;

    @Column(name = "NAME_DESCRIPTION")
    private String nameDescription;
}
