package com.farmaciasperuanas.pmmli.provider.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class ProviderDto{

    private String codigoSap;
    private String concBusq;
    private String correo;
    private String direccion;
    private String nif2;
    private String nombre;
    private String ramo;
    private String region;
    private String ruc;
    private String telefono;
}
