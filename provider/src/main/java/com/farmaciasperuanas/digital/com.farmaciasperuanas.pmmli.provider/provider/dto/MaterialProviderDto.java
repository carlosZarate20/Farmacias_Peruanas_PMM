package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialProviderDto {

    private String codigoLaboratorio;
    private String codigoProveedor;
    private String materialInka;
    private String materialMifa;
    private String proveedorPrincipal;
}
