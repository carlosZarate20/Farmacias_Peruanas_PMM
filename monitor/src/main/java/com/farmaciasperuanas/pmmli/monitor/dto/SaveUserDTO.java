package com.farmaciasperuanas.pmmli.monitor.dto;

import lombok.Data;

@Data
public class SaveUserDTO {
    String username;
    String name;
    String email;
    Long profileId;
}
