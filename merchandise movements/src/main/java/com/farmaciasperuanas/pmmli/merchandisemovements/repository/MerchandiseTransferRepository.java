package com.farmaciasperuanas.pmmli.merchandisemovements.repository;

import com.farmaciasperuanas.pmmli.merchandisemovements.entity.MerchandiseTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchandiseTransferRepository extends JpaRepository<MerchandiseTransfer, Long> {
    @Query(value = "select i.inner_pack_id from pmm.prdpcdee i, pmm.prdmstee p " +
            "where i.prd_lvl_child = p.prd_lvl_child " +
            "and i.loose_pack_flag = 'F' " +
            "and i.sll_units_per_inner = 1 " +
            "and i.inv_units_per_inner = 1 " +
            "and p.prd_lvl_number = :prdLvlNumber " +
            "and rownum = 1", nativeQuery = true)
    Integer getInnerPack(@Param("prdLvlNumber") String prdLvlNumber);

    @Query(value = "SELECT pmm.TRANS_SESSION.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getTransSession();

    @Procedure(name = "java_procedure_merchandise_transfer")
    void storeProcedure(@Param("TRANS_SESSION_VAL") Integer TRANS_SESSION_VAL);

    @Query(value = "select COUNT(*) " +
            "from pmm.FAPINVREJEE " +
            "where TRANS_SESSION = :transSession", nativeQuery = true)
    Integer validateExitsError(@Param("transSession") Integer transSession);

    @Query("select mt from MerchandiseTransfer mt where mt.transSession = :transSession and mt.transSequence = :transSequence")
    MerchandiseTransfer getMerchandiseTransfer(@Param("transSession") Integer transSession, @Param("transSequence") Integer transSequence);
}
