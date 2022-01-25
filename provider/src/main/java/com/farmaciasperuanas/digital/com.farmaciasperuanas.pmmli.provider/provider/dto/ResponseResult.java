package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseResult implements Serializable {

    private static final long serialVersionUID = 1L;
    protected int serverCode;
    protected boolean success;
    protected String message;
    protected boolean estado;
    protected Object results;
}
