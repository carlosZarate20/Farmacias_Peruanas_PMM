package com.farmaciasperuanas.pmmli.provider.repository;

import com.farmaciasperuanas.pmmli.provider.entity.MaterialProvider;
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
public interface MaterialProviderRepository extends JpaRepository<MaterialProvider, Long>, JpaSpecificationExecutor<MaterialProvider> {

    @Query("select mp from MaterialProvider mp where mp.flagLi <> 1")
    List<MaterialProvider> geListMaterialProvider();

    @Modifying
    @Transactional
    @Query(value = "UPDATE SWLI.MATERIAL_PROVIDER " +
            "SET FLAGLI = 1 " +
            "WHERE MATERIAL_INKA = :materialInka and PROVIDER_CODE = :prov", nativeQuery = true)
    void updateMaterialProvider(@Param("materialInka") String materialInka,@Param("prov") String prov);

    @Procedure(name = "java_procedure_update_material_provider")
    void procedureUpdateMaterialProvider();
}
