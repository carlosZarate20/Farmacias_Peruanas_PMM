package com.farmaciasperuanas.pmmli.provider.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "PROVIDER_EXIT", schema = "SWLI")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "java_procedure_salida_proveedor",
                procedureName = "SWLI.PR_SALIDA_PROVEEDOR",
                parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "SESSION_NUMBER_VAL", type = Integer.class)
        }),
        @NamedStoredProcedureQuery(name = "java_procedure_get_inner_pack",
                procedureName = "SWLI.PR_GET_INNER_PACK",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "PRD_LVL_NUMBER_VAL", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name ="INNER_PACK", type = Integer.class)
        })
})
public class ProviderExit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROVIDER_EXIT")
    private Long idProviderExit;

    @Column(name = "SESSION_NUMBER")
    private Integer sessionNumber;

    @Column(name = "TECH_KEY")
    private Integer techKey;

    @Column(name = "PRD_LVL_NUMBER")
    private String prdLvlNumber;

    @Column(name = "RTV_SHIP_LOC")
    private Integer rtvShipLoc;

    @Column(name = "CARRIER_NAME")
    private String carrierName;

    @Column(name = "RTV_CARTON_ID")
    private String rtvCartonId;

    @Column(name = "JDA_ORIGIN")
    private String jdaOrigin;

    @Column(name = "VENDOR_NUMBER")
    private String vendorNumber;

    @Column(name = "RTV_TYPE_ID")
    private String rtvTypeId;

    @Column(name = "REQUESTED_BY")
    private String requestedBy;

    @Column(name = "ACTION_CODE")
    private String actionCode;

    @Column(name = "RTV_PRIOR_ID")
    private String rtvPriorId;

    @Column(name = "QUANTITY")
    private Double quantity;

    @Column(name = "REFERENCE")
    private String reference;

    @Column(name = "DATE_CREATED")
    private Date dateCreated;

    @Column(name = "RTV_DTL_NOTES")
    private String rtvDtlNotes;

    @Column(name = "INNER_PACK_ID")
    private Integer innerPackId;

    @Column(name = "RTV_ENTRY_METHOD")
    private Integer rtvEntryMethod;

    @Column(name = "RTV_LOTE")
    private String rtvLote;

    @Column(name = "RTV_VCTO_LOTE")
    private Date rtvVctoLote;
}
