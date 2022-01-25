package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.dto.DataMaestraDto;

import java.util.List;

public interface TransactionLogService {
    List<DataMaestraDto> getDatosMaestros();
}
