package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.dto.DataResponseDTO;
import com.farmaciasperuanas.pmmli.monitor.dto.DataTableDto;
import com.farmaciasperuanas.pmmli.monitor.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.monitor.dto.user.UserListItemDto;
import com.farmaciasperuanas.pmmli.monitor.dto.user.UserListRequestDto;
import com.farmaciasperuanas.pmmli.monitor.dto.user.UserSignInResponseDTO;
import com.farmaciasperuanas.pmmli.monitor.entity.UserAccess;

import javax.servlet.http.HttpServletRequest;

public interface UserAccessService {
    ResponseDto<UserAccess> saveUser(String username,String name, String email, Long profileId);

    DataResponseDTO<UserSignInResponseDTO> login(String username, String password);

    DataTableDto<UserListItemDto> userList(UserListRequestDto dto);

    UserAccess getUser(Long id);

    ResponseDto<UserAccess> updateUser(Long id,String username,String name, String email, Long profileId,Long state);

    UserAccess getInfo(HttpServletRequest req);

}
