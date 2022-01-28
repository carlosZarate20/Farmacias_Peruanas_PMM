package com.farmaciasperuanas.pmmli.materialinformation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseApiForbidden {
    private Integer status;
    private String message;
    private DataForbidden dataForbidden;
}
