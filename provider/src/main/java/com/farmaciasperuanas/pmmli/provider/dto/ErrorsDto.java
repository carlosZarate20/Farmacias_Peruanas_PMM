package com.farmaciasperuanas.pmmli.provider.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorsDto {
    private String identifier;
    private String message;
}
