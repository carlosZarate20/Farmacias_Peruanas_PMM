package com.farmaciasperuanas.pmmli.merchandisemovements.repository;

import com.farmaciasperuanas.pmmli.merchandisemovements.entity.CdTransferIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CdTransferInRepository extends JpaRepository<CdTransferIn, Long> {

    @Query(value = "select i.inner_pack_id from pmm.prdpcdee i, pmm.prdmstee p " +
            "where i.prd_lvl_child = p.prd_lvl_child " +
            "and i.loose_pack_flag = 'F' " +
            "and i.sll_units_per_inner = 1 " +
            "and i.inv_units_per_inner = 1 " +
            "and p.prd_lvl_number = :prd_lvl_number " +
            "and rownum = 1", nativeQuery = true)
    Integer getInnerPack(@Param("prd_lvl_number") String prd_lvl_number);

    @Query(value = "select pmm.SESSION_NUMBER.nextval from dual", nativeQuery = true)
    Integer getSessionNumber();

    @Procedure(name = "java_procedure_traslado_cd_entrada")
    void storeProcedure(@Param("SESSION_NUMBER_VAL") Integer SESSION_NUMBER_VAL);

    @Procedure(name = "java_procedure_transaction_error_entrada")
    void storeProcedureLogError(@Param("TRANSACTION_LOG_ID") Long TRANSACTION_LOG_ID, @Param("SESSION_NUMBER_VAL") Integer SESSION_NUMBER_VAL,
                                @Param("DESC_PRD_NUMBER_VAL") String DESC_PRD_NUMBER_VAL,
                                @Param("DESC_LOTE_VAL") String DESC_LOTE_VAL,
                                @Param("INDETIFIER_VAL") String INDETIFIER_VAL);

    @Query(value = "select COUNT(*) " +
            "from pmm.FAPSDITRFDTI " +
            "where SESSION_NUMBER = :sessionNumber", nativeQuery = true)
    Integer validateExitsError(@Param("sessionNumber") Integer sessionNumber);

}
