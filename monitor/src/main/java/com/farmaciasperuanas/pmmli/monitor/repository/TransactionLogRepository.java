package com.farmaciasperuanas.pmmli.monitor.repository;

import com.farmaciasperuanas.pmmli.monitor.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long>, JpaSpecificationExecutor<TransactionLog> {

//    @Query(value = "Select Distinct sw.NAME_TRANSACTION, " +
//            "(Select Max(sl.CANT_TRANSACTION) from SWLI.TRANSACTION_LOG sl " +
//            "where sl.CODE = 'M' and sl.CODE_TRANSACTION = sw.CODE_TRANSACTION) AS CANT_TRANSACTION, " +
//            "(Select Max(si.DATE_TRANSACTION) from SWLI.TRANSACTION_LOG si " +
//            "where si.CODE = 'M' and si.CODE_TRANSACTION = sw.CODE_TRANSACTION) AS DATE_TRANSACTION, " +
//            "CODE_TRANSACTION, TYPE_TRANSACTION, TASK_STATE " +
//            "from SWLI.TRANSACTION_LOG sw " +
//            "where sw.CODE = 'M' " +
//            "order by NAME_TRANSACTION", nativeQuery = true)
//    List<Object[]> getDataMaestra();

    @Query(value = "select " +
            "tk.NAME_TASK, " +
            "tk.TYPE_TRANSACTION, " +
            "tk.TASK_STATE, " +
            "tk.CODE_TRANSACTION, " +
            "NVL((Select Max(sl.CANT_TRANSACTION) from SWLI.TRANSACTION_LOG sl " +
            "where sl.CODE = 'M' and sl.CODE_TRANSACTION = tk.CODE_TRANSACTION), 0) AS CANT_TRANSACTION, " +
            "NVL((Select Max(si.DATE_TRANSACTION) from SWLI.TRANSACTION_LOG si " +
            "where si.CODE = 'M' and si.CODE_TRANSACTION = tk.CODE_TRANSACTION), null) AS DATE_TRANSACTION " +
            "from SWLI.TRANSACTION_TASK tk", nativeQuery = true)
    List<Object[]> getDataMaestra();

    @Query(value = "select NAME_TRANSACTION, CANT_TRANSACTION, DATE_TRANSACTION from TRANSACTION_LOG", nativeQuery = true)
    List<Object[]> getTransactionLog();

    @Query(value = "select * from " +
            "(select ID_TRANSACTION_LOG, NAME_TRANSACTION, STATE, DATE_TRANSACTION " +
            "from SWLI.TRANSACTION_LOG " +
            "where CANT_TRANSACTION <> 0 " +
            "order by ID_TRANSACTION_LOG desc) " +
            "where rownum <= 5", nativeQuery = true)
    List<Object[]> getDahsboardTransaction();

    @Query(value = "select " +
            "NAME_TRANSACTION, " +
            "STATE, " +
            "DATE_TRANSACTION, " +
            "JSON_REQUEST, " +
            "JSON_RESPONSE " +
            "from SWLI.TRANSACTION_LOG " +
            "where ID_TRANSACTION_LOG = :id", nativeQuery = true)
    List<Object[]> getTransactionDetail(@Param("id") Integer id);
}
