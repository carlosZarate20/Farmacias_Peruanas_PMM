package com.farmaciasperuanas.pmmli.merchandisemovements.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "TRANSACTION_LOG", schema = "SWLI")
public class TransactionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TRANSACTION_LOG")
    private Long idTransacctionLog;

    @Column(name = "NAME_TRANSACTION")
    private String nameTransaction;

    @Column(name = "SESSION_NUMBER")
    private Integer sessionNumber;

    @Column(name = "CODE")
    private String code;

    @Column(name = "CODE_TRANSACTION")
    private String codeTransaction;

    @Column(name = "TYPE_TRANSACTION")
    private String typeTransaction;

    @Column(name = "CANT_TRANSACTION")
    private Integer cantTransaction;

    @Column(name = "DATE_TRANSACTION")
    private Date dateTransaction;

    @Column(name = "STATE")
    private String state;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "JSON_REQUEST")
    private String jsonRequest;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "JSON_RESPONSE")
    private String jsonResponse;

    @OneToMany(mappedBy = "transactionLog")
    private List<TransactionLogError> transactionLogErrors;
}