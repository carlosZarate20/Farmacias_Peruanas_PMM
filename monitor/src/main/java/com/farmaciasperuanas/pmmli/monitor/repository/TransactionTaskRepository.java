package com.farmaciasperuanas.pmmli.monitor.repository;

import com.farmaciasperuanas.pmmli.monitor.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.monitor.entity.TransactionTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionTaskRepository extends JpaRepository<TransactionTask, Long>, JpaSpecificationExecutor<TransactionLog> {
    @Query(value = "select A.* from SWLI.TRANSACTION_TASK A " +
            "where A.CODE_TRANSACTION = :code", nativeQuery = true)
    TransactionTask getTransactionTaskByCode(@Param("code") String code);
}
