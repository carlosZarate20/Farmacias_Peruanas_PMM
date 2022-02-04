package com.farmaciasperuanas.pmmli.materialinformation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TRANSACTION_LOG_ERROR", schema = "SWLI")
public class TransactionLogError {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TRANSACTION_LOG_ERROR")
    private Long idTransactionLogError;

    //    @ManyToOne
//    @JoinColumn(name = "TRANSACTION_LOG_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSACTION_LOG_ID", referencedColumnName = "ID_TRANSACTION_LOG")
    @JsonIgnore
    private TransactionLog transactionLog;

    @Column(name = "IDENTIFIER")
    private String identifier;

    @Column(name = "MESSAGE")
    private String message;

}