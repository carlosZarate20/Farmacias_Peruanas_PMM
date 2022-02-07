package com.farmaciasperuanas.pmmli.monitor.dto;

import lombok.Data;

@Data
public class SaveUserDTO {
    String username;
    String password;
    String name;
    String email;
    Long profileId;
}
