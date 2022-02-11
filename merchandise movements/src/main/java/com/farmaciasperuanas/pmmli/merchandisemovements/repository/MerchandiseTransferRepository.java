package com.farmaciasperuanas.pmmli.merchandisemovements.repository;

import com.farmaciasperuanas.pmmli.merchandisemovements.entity.MerchandiseTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchandiseTransferRepository extends JpaRepository<MerchandiseTransfer, Long> {

    @Procedure(name = "java_procedure_get_inner_pack_merchandise")
    Integer getInnerPack(@Param("PRD_LVL_NUMBER_VAL") String PRD_LVL_NUMBER_VAL);

    @Query(value = "SELECT pmm.TRANS_SESSION.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getTransSession();

    @Procedure(name = "java_procedure_merchandise_transfer")
    void storeProcedure(@Param("TRANS_SESSION_VAL") Integer TRANS_SESSION_VAL);

    @Query(value = "select COUNT(*) " +
            "from pmm.FAPINVREJEE " +
            "where TRANS_SESSION = :transSession and ERROR_CODE is not null", nativeQuery = true)
    Integer validateExitsError(@Param("transSession") Integer transSession);

    @Query("select mt from MerchandiseTransfer mt where mt.transSession = :transSession and mt.transSequence = :transSequence")
    MerchandiseTransfer getMerchandiseTransfer(@Param("transSession") Integer transSession, @Param("transSequence") Integer transSequence);
}
