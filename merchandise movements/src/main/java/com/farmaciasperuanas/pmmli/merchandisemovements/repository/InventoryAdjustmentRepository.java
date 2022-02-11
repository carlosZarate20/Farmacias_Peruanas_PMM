package com.farmaciasperuanas.pmmli.merchandisemovements.repository;

import com.farmaciasperuanas.pmmli.merchandisemovements.entity.InventoryAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryAdjustmentRepository extends JpaRepository<InventoryAdjustment, Long> {
    @Query(value = "select i.inner_pack_id from pmm.prdpcdee i, pmm.prdmstee p " +
            "where i.prd_lvl_child = p.prd_lvl_child " +
            "and i.loose_pack_flag = 'F' " +
            "and i.sll_units_per_inner = 1 " +
            "and i.inv_units_per_inner = 1 " +
            "and p.prd_lvl_number = :trans_prd_lvl_number " +
            "and rownum = 1", nativeQuery = true)
    Integer getInnerPack(@Param("trans_prd_lvl_number") String trans_prd_lvl_number);

    @Query(value = "SELECT pmm.TRANS_SESSION.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getTransSession();

    @Procedure(name = "java_procedure_inventory_adjustment")
    void storeProcedure(@Param("TRANS_SESSION_VAL") Integer TRANS_SESSION_VAL);

    @Query(value = "select COUNT(*) " +
            "from pmm.FAPINVREJEE " +
            "where TRANS_SESSION = :transSession", nativeQuery = true)
    Integer validateExitsError(@Param("transSession") Integer transSession);

    @Query("select ia from InventoryAdjustment ia where ia.transSession = :transSession and ia.transSequence = :transSequence")
    InventoryAdjustment getInventoryAdjustment(@Param("transSession") Integer transSession, @Param("transSequence") Integer transSequence);
}
