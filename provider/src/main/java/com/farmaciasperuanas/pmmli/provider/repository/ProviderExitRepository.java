package com.farmaciasperuanas.pmmli.provider.repository;

import com.farmaciasperuanas.pmmli.provider.dto.ErrorsDto;
import com.farmaciasperuanas.pmmli.provider.entity.ProviderExit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderExitRepository extends JpaRepository<ProviderExit, Long>{

    @Procedure(name = "java_procedure_get_inner_pack")
    Integer getInnerPackProcedure(@Param("PRD_LVL_NUMBER_VAL") String PRD_LVL_NUMBER_VAL);

    @Query(value = "select pmm.SESSION_NUMBER.nextval from dual", nativeQuery = true)
    Integer getSessionNumber();

    @Procedure(name = "java_procedure_salida_proveedor")
    void storeProcedure(@Param("SESSION_NUMBER_VAL") Integer SESSION_NUMBER_VAL);

    @Query(value = "select COUNT(*) " +
            "from pmm.FAPSDIRTVDTI " +
            "where SESSION_NUMBER = :sessionNumber", nativeQuery = true)
    Integer validateExitsError(@Param("sessionNumber") Integer sessionNumber);

    @Query("select p from ProviderExit p where p.sessionNumber = :sessionNumber and p.techKey = :techKey")
    ProviderExit getProviderExit(@Param("sessionNumber") Integer sessionNumber, @Param("techKey") Integer techKey);

}
