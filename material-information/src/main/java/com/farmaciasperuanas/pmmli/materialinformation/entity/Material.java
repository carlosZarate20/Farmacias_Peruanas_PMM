package com.farmaciasperuanas.pmmli.materialinformation.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "MATERIAL", schema = "SWLI")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "java_procedure_update_material",
                procedureName = "SWLI.PR_UPDATE_MATERIAL")
})
public class Material implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MATERIAL")
    private Long idMaterial;

    @Column(name = "INKA")
    private String inka;

    @Column(name = "MIFA")
    private String mifa;

    @Column(name = "PRD_LVL_CHILD")
    private Integer prdLvlChild;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "UNIT_PRICE")
    private Double unitPrice;

    @Column(name = "CONTROL")
    private Integer control;

    @Column(name = "ASSORTMENT")
    private String assortment;

    @Column(name = "INDUCTION_ZONE")
    private String inductionZone;

    @Column(name = "FAMILY")
    private String family;

    @Column(name = "FAMILY_CODE")
    private String familyCode;

    @Column(name = "COOLED")
    private Integer cooled;

    @Column(name = "INACTIVE")
    private Integer inactive;

    @Column(name = "DENOM")
    private String denom;

    @Column(name = "UPDATED")
    private Long updated;

    @Column(name = "FLAGLI")
    private Long flagLi;
}
