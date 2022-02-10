package com.farmaciasperuanas.pmmli.monitor.dto.user;

import lombok.Data;

@Data
public class UserSignInResponseDTO {
    public String token;

    public UserSignInResponseDTO(){
        this.token = "";
    }

    public UserSignInResponseDTO(String token) {
        this.token = token;
    }
}
