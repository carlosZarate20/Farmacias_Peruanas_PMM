package com.farmaciasperuanas.pmmli.materialinformation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VolumetricDataDto {
    private Double altura;
    private Double ancho;
    private Double contador;
    private Double demon;
    private String eanUpc;
    private Double longitud;
    private String material;
    private Double pesoBruto;
    private String tp;
    private String uma;
    private String unidad;
    private String unidadPeso;
    private String unidadVolumen;
    private Double volumen;
}
