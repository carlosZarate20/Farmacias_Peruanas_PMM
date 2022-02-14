package com.farmaciasperuanas.pmmli.localstore.repository;

import com.farmaciasperuanas.pmmli.localstore.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, JpaSpecificationExecutor<Store> {
    @Query("select s from Store s where s.flagLi <> 1")
    List<Store> getListStore();

    @Modifying
    @Transactional
    @Query(value = "UPDATE SWLI.STORE " +
            "SET FLAGLI = 1 " +
            "WHERE CODE = :code", nativeQuery = true)
    void updateStore(@Param("code") String code);

    @Procedure(name = "java_procedure_update_store")
    void procedureUpdate();
}
