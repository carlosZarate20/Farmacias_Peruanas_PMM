package com.farmaciasperuanas.pmmli.localstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDto {

    private String calleNumero;
    private String codigo;
    private String codigoFarma;
    private String correo;
    private String departamento;
    private String direccion;
    private String distrito;
    private String nombre;
    private String provincia;
    private String sociedad;
    private String tipoLocal;
}
