package com.farmaciasperuanas.pmmli.localstore.dto;

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
