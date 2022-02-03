package com.farmaciasperuanas.pmmli.localstore.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "TRANSACTION_LOG_ERROR", schema = "SWLI")
public class TransactionLogError {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TRANSACTION_LOG_ERROR")
    private Long idTransactionLogError;

    @ManyToOne
    @JoinColumn(name = "TRANSACTION_LOG_ID")
    private TransactionLog transactionLog;

    @Column(name = "IDENTIFIER")
    private String identifier;

    @Column(name = "MESSAGE")
    private String message;

}