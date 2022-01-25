package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private String code;
    private String[] errors;
}
