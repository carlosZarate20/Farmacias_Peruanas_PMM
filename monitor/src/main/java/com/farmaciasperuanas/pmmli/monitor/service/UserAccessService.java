package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.monitor.entity.UserAccess;

public interface UserAccessService {
    ResponseDto<UserAccess> saveUser(String username, String password, String name, String email, Long profileId);
}
