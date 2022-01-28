package com.farmaciasperuanas.pmmli.provider.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String codigoSap;
    private String concBusq;
    private String correo;
    private String direccion;
    private String nif2;
    private String nobmre;
    private String ramo;
    private String region;
    private String ruc;
    private String telefono;
}
