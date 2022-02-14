package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.dto.*;

import java.util.List;

public interface TransactionLogService {
    List<DataMaestraDto> getDatosMaestros();

    List<TransactionLogDto> listarTransactionDashboard();

    TransanctionDetailDto getDetailTransaction(Long id);

    CantMaestroDto getCantidadDatosMonth();

    List<TransactionDto> getNameTransaction();

    List<ErrorTypeDto> getListError();

    DataTableDto<TransactionLogDto> listarTransactionLog(TransactionLogRequestDto transactionLogRequestDto);

    List<PolarChartInfoDto> lastSixMonthsTransactions();
}
