package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "TRANSACCION_LOG", schema = "SWLI")
public class TransactionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TRANSACTION_LOG")
    private Integer idTransacctionLog;

    @Column(name = "TRANSACTION")
    private String transaction;

    @Column(name = "TYPE_OP")
    private String typeOp;

    @Column(name = "TYPE_TRANSACTION")
    private String typeTransaction;

    @Column(name = "CANT_TRANSACTION")
    private Integer cantTransaction;

    @Column(name = "DATE_TRANSACTION")
    private Date dateTransaction;

    @Column(name = "STATE")
    private String state;

    @Column(name = "TASK_STATE")
    private String taskState;

    @Column(name = "CRON_EXPRESSION")
    private String cronExpression;

    @Column(name = "JSON_REQUEST")
    private String jsonRequest;

    @Column(name = "JSON_RESPONSE")
    private String jsonResponse;
}
