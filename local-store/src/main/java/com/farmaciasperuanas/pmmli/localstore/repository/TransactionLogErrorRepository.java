package com.farmaciasperuanas.pmmli.localstore.repository;

import com.farmaciasperuanas.pmmli.localstore.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.localstore.entity.TransactionLogError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TransactionLogErrorRepository extends JpaRepository<TransactionLogError, Long>, JpaSpecificationExecutor<TransactionLogError> {
    @Query("SELECT T FROM TransactionLogError T where T.transactionLog.idTransacctionLog =:idTransacctionLog ")
    List<TransactionLogError> getTransactionLogError(@Param("idTransacctionLog") Long idTransacctionLog);
}
