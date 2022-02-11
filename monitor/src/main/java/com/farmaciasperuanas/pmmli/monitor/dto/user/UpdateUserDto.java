package com.farmaciasperuanas.pmmli.monitor.dto.user;

import lombok.Data;

@Data
public class UpdateUserDto {
    Long id;
    String username;
    String name;
    String email;
    Long profileId;
    Long state;
}
