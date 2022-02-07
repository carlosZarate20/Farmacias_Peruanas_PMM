package com.farmaciasperuanas.pmmli.merchandisemovements.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseApi{
    private String code;
//    private List<ResponseApiErrorItem> errors;
}