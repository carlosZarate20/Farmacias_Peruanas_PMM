package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.repository;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long>, JpaSpecificationExecutor<Material> {
    @Query("select m from Material m where m.flagLi <> 1")
    List<Material> getListMaterial();

    @Modifying
    @Transactional
    @Query(value = "UPDATE SWLI.MATERIAL " +
            "SET FLAGLI = 1 " +
            "WHERE INKA = :inka", nativeQuery = true)
    void updateMaterial(@Param("inka") String inka);
}
