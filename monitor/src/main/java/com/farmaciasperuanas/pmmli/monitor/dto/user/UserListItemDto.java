package com.farmaciasperuanas.pmmli.monitor.dto.user;

import lombok.Data;

@Data
public class UserListItemDto {
    private Long userId;
    private String name;
    private String username;
    private Long state;
    private String profile;

    public UserListItemDto(Long userId, String name, String username, Long state, String profile) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.state = state;
        this.profile = profile;
    }
}
