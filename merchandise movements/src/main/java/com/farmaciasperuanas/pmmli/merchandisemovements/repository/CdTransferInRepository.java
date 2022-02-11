package com.farmaciasperuanas.pmmli.merchandisemovements.repository;

import com.farmaciasperuanas.pmmli.merchandisemovements.entity.CdTransferIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CdTransferInRepository extends JpaRepository<CdTransferIn, Long> {

    @Procedure(name = "java_procedure_get_inner_pack_in")
    Integer getInnerPack(@Param("PRD_LVL_NUMBER_VAL") String PRD_LVL_NUMBER_VAL);

    @Query(value = "select pmm.SESSION_NUMBER.nextval from dual", nativeQuery = true)
    Integer getSessionNumber();

    @Procedure(name = "java_procedure_traslado_cd_entrada")
    void storeProcedure(@Param("SESSION_NUMBER_VAL") Integer SESSION_NUMBER_VAL);

    @Query(value = "select COUNT(*) " +
            "from pmm.FAPSDITRFDTI " +
            "where SESSION_NUMBER = :sessionNumber and ERROR_CODE is not null", nativeQuery = true)
    Integer validateExitsError(@Param("sessionNumber") Integer sessionNumber);

    @Query("select cdi from CdTransferIn cdi where cdi.sessionNumber = :sessionNumber and cdi.techKey = :techKey")
    CdTransferIn getCdTransferIn(@Param("sessionNumber") Integer sessionNumber, @Param("techKey") Integer techKey);

}
