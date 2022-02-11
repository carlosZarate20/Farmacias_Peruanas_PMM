package com.farmaciasperuanas.pmmli.localstore.repository;

import com.farmaciasperuanas.pmmli.localstore.entity.LocalReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalReturnRepository extends JpaRepository<LocalReturn, Long> {

    @Procedure(name = "java_procedure_get_inner_pack")
    Integer getInnerPack(@Param("PRD_LVL_NUMBER_VAL") String PRD_LVL_NUMBER_VAL);

    @Query(value = "select pmm.SESSION_NUMBER.nextval from dual", nativeQuery = true)
    Integer getSessioNumber();

    @Procedure(name = "java_procedure_local_return")
    void storeProcedureLocalReturn(@Param("SESSION_NUMBER_VAL") Integer SESSION_NUMBER_VAL);

    @Query(value = "select COUNT(*) " +
            "from pmm.FAPSDITRFDTI " +
            "where SESSION_NUMBER = :sessionNumber", nativeQuery = true)
    Integer validateExitsError(@Param("sessionNumber") Integer sessionNumber);

    @Query("select lr from LocalReturn lr where lr.sessionNumber = :sessionNumber and lr.techKey = :techKey")
    LocalReturn getLocalReturn(@Param("sessionNumber") Integer sessionNumber, @Param("techKey") Integer techKey);
}
