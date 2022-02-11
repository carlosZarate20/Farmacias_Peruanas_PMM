package com.farmaciasperuanas.pmmli.merchandisemovements.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "MERCHANDISE_TRANSFER", schema = "SWLI")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "java_procedure_merchandise_transfer",
                procedureName = "SWLI.PR_MERCHANDISE_TRANSFER",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "TRANS_SESSION_VAL", type = Integer.class)
                })
})
public class MerchandiseTransfer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MERCHANDISE_TRANSFER")
    private Long idMerchandiseTransfer;

    @Column(name = "TRANS_SESSION")
    private Integer transSession;

    @Column(name = "TRANS_USER")
    private String transUser;

    @Column(name = "TRANS_BATCH_DATE")
    private Date transBatchDate;

    @Column(name = "TRANS_SOURCE")
    private String transSource;

    @Column(name = "TRANS_AUDITED")
    private String transAudited;

    @Column(name = "TRANS_SEQUENCE")
    private Integer transSequence;

    @Column(name = "TRANS_TRN_CODE")
    private String transTrnCode;

    @Column(name = "TRANS_TYPE_CODE")
    private String transTypeCode;

    @Column(name = "TRANS_DATE")
    private Date transDate;

    @Column(name = "INV_MRPT_CODE")
    private String invMrptCode;

    @Column(name = "INV_DRPT_CODE")
    private String invDrptCode;

    @Column(name = "TRANS_CURR_CODE")
    private String transCurrCode;

    @Column(name = "TRANS_ORG_LVL_NUMBER")
    private Integer transOrgLvlNumber;

    @Column(name = "TRANS_PRD_LVL_NUMBER")
    private String transPrdLvlNumber;

    @Column(name = "PROC_SOURCE")
    private String procSource;

    @Column(name = "TRANS_QTY")
    private Integer transQty;

    @Column(name = "INNER_PACK_ID")
    private Integer innerPackId;

    @Column(name = "TRANS_INNERS")
    private Integer transInners;

    @Column(name = "TRANS_LOTE")
    private String transLote;

    @Column(name = "TRANS_VCTO_LOTE")
    private Date transVctoLote;
}
