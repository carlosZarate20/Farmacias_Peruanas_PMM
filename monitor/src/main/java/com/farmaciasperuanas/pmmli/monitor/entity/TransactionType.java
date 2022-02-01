package com.farmaciasperuanas.pmmli.monitor.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TRANSACTION_TYPE", schema = "SWLI")
public class TransactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TRANSACTION_TYPE")
    private Integer idTransactionType;

    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;

    @Column(name = "NAME_TRANSACTION")
    private String nameTransaction;
}
