package com.farmaciasperuanas.pmmli.provider.dto;

import lombok.Data;

@Data
public class ResponseApiErrorItem {
    private String pk;
    private String message;
    private Integer position;
}
