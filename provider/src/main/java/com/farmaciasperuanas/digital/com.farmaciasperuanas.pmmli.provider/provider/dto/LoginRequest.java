package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.provider.provider.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    private String username;
    private String password;
}
