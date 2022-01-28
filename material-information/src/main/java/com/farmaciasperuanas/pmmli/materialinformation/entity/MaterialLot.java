package com.farmaciasperuanas.pmmli.materialinformation.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "MATERIAL_LOT", schema = "SWLI.")
public class MaterialLot implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MATERIAL_LOT")
    private Integer idMaterialLot;

    @Column(name = "MATERIAL_CODE")
    private String materialCode;

    @Column(name = "LOTE")
    private String lote;

    @Column(name = "EXPIRE_DATE")
    private Date expireDate;

    @Column(name = "UPDATED")
    private Long updated;

    @Column(name = "FLAGLI")
    private Long flagLi;
}
