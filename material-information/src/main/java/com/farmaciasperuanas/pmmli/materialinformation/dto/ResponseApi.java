package com.farmaciasperuanas.pmmli.materialinformation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseApi {
    private String code;
    private Long id;
    private List<ResponseApiErrorItem> errors;
}
