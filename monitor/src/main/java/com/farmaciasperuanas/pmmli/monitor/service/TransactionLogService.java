package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.dto.CantMaestroDto;
import com.farmaciasperuanas.pmmli.monitor.dto.DataMaestraDto;
import com.farmaciasperuanas.pmmli.monitor.dto.TransactionDto;
import com.farmaciasperuanas.pmmli.monitor.dto.TransanctionDetailDto;

import java.util.List;

public interface TransactionLogService {
    List<DataMaestraDto> getDatosMaestros();

    List<TransactionDto> listarTransactionDashboard();

    TransanctionDetailDto getDetailTransaction(Integer id);

    CantMaestroDto getCantidadDatosMonth();
}
