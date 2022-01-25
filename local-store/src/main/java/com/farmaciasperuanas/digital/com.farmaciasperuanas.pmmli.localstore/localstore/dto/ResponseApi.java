package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.localstore.localstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseApi {
    private String code;
    private String[] errors;
}
