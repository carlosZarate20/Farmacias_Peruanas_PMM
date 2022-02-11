package com.farmaciasperuanas.pmmli.merchandisemovements.repository;

import com.farmaciasperuanas.pmmli.merchandisemovements.entity.InventoryAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryAdjustmentRepository extends JpaRepository<InventoryAdjustment, Long> {

    @Procedure(name = "java_procedure_get_inner_pack_inventory")
    Integer getInnerPack(@Param("PRD_LVL_NUMBER_VAL") String PRD_LVL_NUMBER_VAL);

    @Query(value = "SELECT pmm.TRANS_SESSION.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getTransSession();

    @Procedure(name = "java_procedure_inventory_adjustment")
    void storeProcedure(@Param("TRANS_SESSION_VAL") Integer TRANS_SESSION_VAL);

    @Query(value = "select COUNT(*) " +
            "from pmm.FAPINVREJEE " +
            "where TRANS_SESSION = :transSession and ERROR_CODE is not null", nativeQuery = true)
    Integer validateExitsError(@Param("transSession") Integer transSession);

    @Query("select ia from InventoryAdjustment ia where ia.transSession = :transSession and ia.transSequence = :transSequence")
    InventoryAdjustment getInventoryAdjustment(@Param("transSession") Integer transSession, @Param("transSequence") Integer transSequence);
}
