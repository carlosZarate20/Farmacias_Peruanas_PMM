package com.farmaciasperuanas.pmmli.materialinformation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialDto {

    private Integer control;
    private String descripcion;
    private String descripcionGrupoArticulo;
    private String familia;
    private String grupoArticulo;
    private String inka;
    private String mifa;
    private Double precioUnitario;
    private String surtido;
    private String zonaInduccion;
    private String codigoFamilia;
    private Integer cooled;
    private Integer inactive;
}
