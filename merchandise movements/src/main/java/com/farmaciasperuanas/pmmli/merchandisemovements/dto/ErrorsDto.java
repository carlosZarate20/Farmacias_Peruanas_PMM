package com.farmaciasperuanas.pmmli.merchandisemovements.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorsDto {
    private String identifier;
    private String message;
}
