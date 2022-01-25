package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.repository;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long>, JpaSpecificationExecutor<TransactionLog> {

    @Query(value = "select MAX(CANT_TRANSACTION) as CANT_TRANSACTION from SWLI.TRANSACTION_LOG where TYPE_TRANSACTION = :type", nativeQuery = true)
    Integer getCanTransaction(@Param("type") String type);

//    @Query(value = "select Count(*) from SWLI.TRANSACTION_LOG where TYPE_TRANSACTION = :type", nativeQuery = true)
//    Integer getCountTransaction(@Param("type") String type);

    @Modifying
    @Transactional
    @Query(value = "update SWLI.TRANSACTION_LOG " +
            "set CANT_TRANSACTION = :cantTransaction, " +
            "DATE_TRANSACTION = NVL((to_date(:date, 'DD/MM/YYYY HH24:MI:SS')), NULL)" +
            "where TYPE_TRANSACTION = :type ", nativeQuery = true)
    void updateTransaction(@Param("cantTransaction") Integer cantTransaction,
                           @Param("type") String type, @Param("date") String date);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO SWLI.TRANSACTION_LOG (NAME_TRANSACTION, "
            + " CODE, "
            + " CODE_TRANSACTION, "
            + " TYPE_TRANSACTION, "
            + " CANT_TRANSACTION, "
            + " DATE_TRANSACTION, "
            + " STATE, "
            + " JSON_REQUEST, "
            + " JSON_RESPONSE)" +
            " VALUES (:nameTrasanction, "
            + " :codeOp, "
            + " :codeTransaction, "
            + " :typeTransaction, "
            + " :cantTransaction, "
            + " NVL((to_date(:dateTransaction, 'DD/MM/YYYY HH24:MI:SS')), NULL), "
            + " :state, "
            + " :jsonRequest, "
            + " :jsonResponse)", nativeQuery = true)
    void saveTransactionLog(@Param("nameTrasanction") String nameTrasanction,
                            @Param("codeOp") String codeOp,
                            @Param("codeTransaction") String codeTransaction,
                            @Param("typeTransaction") String typeTransaction,
                            @Param("cantTransaction") Integer cantTransaction,
                            @Param("dateTransaction") String dateTransaction,
                            @Param("state") String state,
                            @Param("jsonRequest") String jsonRequest,
                            @Param("jsonResponse") String jsonResponse);
}