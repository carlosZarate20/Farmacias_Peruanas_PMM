package com.farmaciasperuanas.pmmli.localstore.service;

import com.farmaciasperuanas.pmmli.localstore.dto.LocalReturnDto;
import com.farmaciasperuanas.pmmli.localstore.dto.ResponseDto;

import java.util.List;

public interface LocalReturnService {
    ResponseDto enviarExtornosLi(List<LocalReturnDto> localReturnDtoList);
}
