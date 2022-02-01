package com.farmaciasperuanas.pmmli.monitor.repository;

import com.farmaciasperuanas.pmmli.monitor.entity.TransactionLog;
import org.springframework.data.domain.Pageable;
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
            "where si.CODE = 'M' and si.CODE_TRANSACTION = tk.CODE_TRANSACTION), null) AS DATE_TRANSACTION, " +
            "tk.CRON_EXPRESSION " +
            "from SWLI.TRANSACTION_TASK tk ", nativeQuery = true)
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


    @Query(value = "select distinct " +
            "(select Count(*) from SWLI.TRANSACTION_LOG " +
            "where CODE = 'M' and STATE = 'F') as MAESTRO_FALLIDO, " +
            "(select Count(*) from SWLI.TRANSACTION_LOG " +
            "where CODE = 'M' and STATE = 'C') as MAESTRO_CORRECTO, " +
            "(select Count(*) from SWLI.TRANSACTION_LOG  " +
            "where CODE = 'T' and STATE = 'F') as TRANMSACTION_FALLIDO, " +
            "(select Count(*) from SWLI.TRANSACTION_LOG " +
            "where CODE = 'T' and STATE = 'C') as TRANSACTION_CORRECTO, " +
            "(select Count(*) from SWLI.TRANSACTION_LOG " +
            "where CODE = 'M') as MAESTRO_TOTAL, " +
            "(select Count(*) from SWLI.TRANSACTION_LOG " +
            "where CODE = 'T') as TRANSACTION_TOTAL," +
            "(select Count(*) from SWLI.TRANSACTION_LOG " +
            "where CODE IN ('M', 'T')) as TOTAL " +
            "from SWLI.TRANSACTION_LOG\n" +
            "where DATE_TRANSACTION >= to_date (:date || :month, 'YYYYMM') " +
            "and DATE_TRANSACTION <= add_months(to_date (:date || :month, 'YYYYMM'), 1)", nativeQuery = true)
    List<Object[]> getCantTransactionMonth(@Param("date") String date, @Param("month") String month);

//    @Query("SELECT t.idTransacctionLog, t.nameTransaction, t.state, t.dateTransaction " +
//            "FROM TransactionLog t " +
//            "WHERE ((:validateDate = -1) OR ((:validateDate <> -1) and (t.dateTransaction BETWEEN (to_date(:intiDate, 'DD/MM/YYYY HH24:MI:SS') AND to_date(:finalDate, 'DD/MM/YYYY HH24:MI:SS')))) " +
//            "and ((:validateState = -1) OR ((:validateState <> -1) and (t.state = :state)))" +
//            "and ((:validateTransaction = -1) OR ((:validateTransaction <> -1) and (t.codeTransaction = :codeTransaction)))")
//    List<TransactionLog> getTransactionLogFilter(@Param("intiDate") String intiDate,
//                                          @Param("finalDate") String finalDate,
//                                          @Param("state") String state,
//                                          @Param("codeTransaction") String codeTransaction,
//                                          @Param("validateDate") Integer validateDate,
//                                          @Param("validateTransaction") Integer validateTransaction,
//                                          @Param("validateState") Integer validateState,
//                                          Pageable pageable);

//    @Query(value = "SELECT ID_TRANSACTION_LOG, NAME_TRANSACTION, STATE, DATE_TRANSACTION " +
//            "FROM SWLI.TRANSACTION_LOG  " +
//            "WHERE ((:validateDate = -1) OR ((:validateDate <> -1) and (DATE_TRANSACTION BETWEEN to_date(:intiDate, 'DD/MM/YYYY HH24:MI:SS') AND to_date(:finalDate, 'DD/MM/YYYY HH24:MI:SS')))) " +
//            "and ((:validateState = -1) OR ((:validateState <> -1) and (STATE = :state)))" +
//            "and ((:validateTransaction = -1) OR ((:validateTransaction <> -1) and (CODE_TRANSACTION = :codeTransaction)))",
//            nativeQuery = true)
//    List<Object[]> getTransactionLogFilter(@Param("intiDate") String intiDate,
//                                                          @Param("finalDate") String finalDate,
//                                                          @Param("state") String state,
//                                                          @Param("codeTransaction") String codeTransaction,
//                                                          @Param("validateDate") Integer validateDate,
//                                                          @Param("validateTransaction") Integer validateTransaction,
//                                                          @Param("validateState") Integer validateState,
//                                                          Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM SWLI.TRANSACTION_LOG  " +
            "WHERE ((:validateDate = -1) OR ((:validateDate <> -1) and (DATE_TRANSACTION BETWEEN to_date(:intiDate, 'DD/MM/YYYY HH24:MI:SS') AND to_date(:finalDate, 'DD/MM/YYYY HH24:MI:SS')))) " +
            "and ((:validateState = -1) OR ((:validateState <> -1) and (STATE = :state)))" +
            "and ((:validateTransaction = -1) OR ((:validateTransaction <> -1) and (CODE_TRANSACTION = :codeTransaction)))",
            nativeQuery = true)
    List<TransactionLog> getTransactionLogFilter(@Param("intiDate") String intiDate,
                                           @Param("finalDate") String finalDate,
                                           @Param("state") String state,
                                           @Param("codeTransaction") String codeTransaction,
                                           @Param("validateDate") Integer validateDate,
                                           @Param("validateTransaction") Integer validateTransaction,
                                           @Param("validateState") Integer validateState,
                                           Pageable pageable);

    @Query(value = "SELECT COUNT(*)" +
            "FROM SWLI.TRANSACTION_LOG  " +
            "WHERE ((:validateDate = -1) OR ((:validateDate <> -1) and (DATE_TRANSACTION BETWEEN to_date(:intiDate, 'DD/MM/YYYY HH24:MI:SS') AND to_date(:finalDate, 'DD/MM/YYYY HH24:MI:SS')))) " +
            "and ((:validateState = -1) OR ((:validateState <> -1) and (STATE = :state)))" +
            "and ((:validateTransaction = -1) OR ((:validateTransaction <> -1) and (CODE_TRANSACTION = :codeTransaction)))",
            nativeQuery = true)
    Integer getTransactionLogFilterCount(@Param("intiDate") String intiDate,
                                                 @Param("finalDate") String finalDate,
                                                 @Param("state") String state,
                                                 @Param("codeTransaction") String codeTransaction,
                                                 @Param("validateDate") Integer validateDate,
                                                 @Param("validateTransaction") Integer validateTransaction,
                                                 @Param("validateState") Integer validateState);
}
