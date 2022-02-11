package com.farmaciasperuanas.pmmli.monitor.dto;

import lombok.Data;

@Data
public class ResponseApiErrorItem {
    private String pk;
    private String message;
    private Integer position;
}
