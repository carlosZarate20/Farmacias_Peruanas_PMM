package com.farmaciasperuanas.pmmli.monitor.dto.user;

import lombok.Data;

@Data
public class UserListRequestDto {
    private String name;
    private Long profileId;
    private Integer page;
    private Integer rows;
}
