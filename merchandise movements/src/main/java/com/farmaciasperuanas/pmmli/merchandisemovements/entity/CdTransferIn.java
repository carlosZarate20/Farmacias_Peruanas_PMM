package com.farmaciasperuanas.pmmli.merchandisemovements.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "CD_TRANSFER_IN", schema = "SWLI")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "java_procedure_traslado_cd_entrada",
                procedureName = "SWLI.PR_TRASLADO_CD_ENTRADA",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "SESSION_NUMBER_VAL", type = Integer.class)
                }),
        @NamedStoredProcedureQuery(
                name = "java_procedure_transaction_error_entrada",
                procedureName = "SWLI.PR_SAVE_TRANSACTION_LOG_ERROR",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "TRANSACTION_LOG_ID", type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "SESSION_NUMBER_VAL", type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "DESC_PRD_NUMBER_VAL", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "DESC_LOTE_VAL", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "INDETIFIER_VAL", type = String.class)
                })
})
public class CdTransferIn implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CD_TRANSFER_IN")
    private Long idCdTransferIn;

    @Column(name = "SESSION_NUMBER")
    private Integer sessionNumber;

    @Column(name = "TECH_KEY")
    private Integer techKey;

    @Column(name = "TRF_NUMBER")
    private Integer trfNumber;

    @Column(name = "CARRIER_NAME", nullable = true)
    private String carrierName;

    @Column(name = "CARTON_NUMBER", nullable = true)
    private String cartonNumber;

    @Column(name = "PRD_LVL_NUMBER")
    private String prdLvlNumber;

    @Column(name = "JDA_ORIGIN")
    private String jdaOrigin;

    @Column(name = "TRF_REASON_CODE")
    private String trfReasonCode;

    @Column(name = "FROM_LOC")
    private Integer fromLoc;

    @Column(name = "TO_LOC")
    private Integer toLoc;

    @Column(name = "QUANTITY")
    private Double quantity;


    @Column(name = "ACTION_CODE")
    private String actionCode;

    @Column(name = "DATE_CREATED")
    private Date dateCreated;

    @Column(name = "REQUESTED_BY")
    private String requestedBy;

    @Column(name = "INNER_PACK_ID")
    private Integer innerPackId;

    @Column(name = "TRF_QTY_FLAG")
    private String trfQtyFlag;
    @Column(name = "TRF_SOURCE_ID")
    private String trfSourceId;
    @Column(name = "TRF_LOTE")
    private String trfLote;
    @Column(name = "TRF_VCTO_LOTE")
    private Date trfVctoLote;

}