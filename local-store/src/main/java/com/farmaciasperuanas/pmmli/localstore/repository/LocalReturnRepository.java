package com.farmaciasperuanas.pmmli.localstore.repository;

import com.farmaciasperuanas.pmmli.localstore.entity.LocalReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalReturnRepository extends JpaRepository<LocalReturn, Long> {

    @Query(value = "select i.inner_pack_id from pmm.prdpcdee i, pmm.prdmstee p " +
            "where i.prd_lvl_child = p.prd_lvl_child " +
            "and i.loose_pack_flag = 'F' " +
            "and i.sll_units_per_inner = 1 " +
            "and i.inv_units_per_inner = 1 " +
            "and p.prd_lvl_number = :prd_lvl_number " +
            "and rownum = 1", nativeQuery = true)
    Integer getInnerPack(@Param("prd_lvl_number") String prd_lvl_number);

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
