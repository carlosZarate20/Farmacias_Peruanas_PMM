package com.farmaciasperuanas.pmmli.monitor.repository;

import com.farmaciasperuanas.pmmli.monitor.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long>, JpaSpecificationExecutor<TransactionLog> {

    @Query(value = "Select Distinct sw.NAME_TRANSACTION, " +
            "(Select Max(sl.CANT_TRANSACTION) from SWLI.TRANSACTION_LOG sl " +
            "where sl.CODE = 'M' and sl.CODE_TRANSACTION = sw.CODE_TRANSACTION) AS CANT_TRANSACTION, " +
            "(Select Max(si.DATE_TRANSACTION) from SWLI.TRANSACTION_LOG si " +
            "where si.CODE = 'M' and si.CODE_TRANSACTION = sw.CODE_TRANSACTION) AS DATE_TRANSACTION, " +
            "CODE_TRANSACTION, TYPE_TRANSACTION, TASK_STATE " +
            "from SWLI.TRANSACTION_LOG sw " +
            "where sw.CODE = 'M' " +
            "order by NAME_TRANSACTION", nativeQuery = true)
    List<Object[]> getDataMaestra();

    @Query(value = "select NAME_TRANSACTION, CANT_TRANSACTION, DATE_TRANSACTION from TRANSACTION_LOG", nativeQuery = true)
    List<Object[]> getTransactionLog();
}
