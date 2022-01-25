package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto<A> {
    private boolean status;
    private int code;
    private A body;
    private String message;
}
