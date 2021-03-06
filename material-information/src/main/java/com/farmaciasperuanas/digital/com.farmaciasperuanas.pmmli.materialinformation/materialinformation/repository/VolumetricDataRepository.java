package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.repository;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.entity.VolumetricData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface VolumetricDataRepository extends JpaRepository<VolumetricData, Long>, JpaSpecificationExecutor<VolumetricData> {

    @Query("select vd from VolumetricData vd where vd.flagLi <> 1")
    List<VolumetricData> getVolumetricDataList();

    @Modifying
    @Transactional
    @Query(value = "UPDATE SWLI.VOLUMETRIC_DATA " +
            "SET FLAGLI = 1 " +
            "WHERE MATERIAL_CODE = :materialCode", nativeQuery = true)
    void updateVolumetricData(@Param("materialCode") String materialCode);
}
