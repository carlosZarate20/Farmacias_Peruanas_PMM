package com.farmaciasperuanas.pmmli.localstore.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "LOCAL_RETURN", schema = "SWLI")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "java_procedure_local_return",
                procedureName = "SWLI.PR_LOCAL_RETURN",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "SESSION_NUMBER_VAL", type = Integer.class)
                })
})
public class LocalReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LOCAL_RETURN")
    private Long idLocalReturn;

    @Column(name = "SESSION_NUMBER")
    private Integer sessionNumber;

    @Column(name = "TECH_KEY")
    private Integer techKey;

    @Column(name = "CARRIER_NAME")
    private String carrierName;

    @Column(name = "CARTON_NUMBER")
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
