package com.farmaciasperuanas.pmmli.monitor.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TRANSACTION_TASK", schema = "SWLI")
public class TransactionTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TRANSACTION_TASK")
    private Integer idTransactionTask;

    @Column(name = "NAME_TRANSACTION")
    private String nameTransaction;

    @Column(name = "CODE")
    private String code;

    @Column(name = "CODE_TRANSACTION")
    private String codeTransaction;

    @Column(name = "TYPE_TRANSACTION")
    private String typeTransaction;

    @Column(name = "TASK_STATE")
    private String taskState;

    @Column(name = "CRON_EXPRESSION")
    private String cronExpression;
}
