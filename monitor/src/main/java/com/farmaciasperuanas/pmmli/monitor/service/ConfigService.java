package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.monitor.entity.ConfigMonitor;
import com.farmaciasperuanas.pmmli.monitor.entity.UserAccess;

public interface ConfigService {
    ResponseDto<String> testEmail(String subject,String to, String body);

    ConfigMonitor getInfo();

    ResponseDto<String> saveConfig(Long id, String email);
}
