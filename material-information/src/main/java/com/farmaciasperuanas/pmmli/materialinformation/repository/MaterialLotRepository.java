package com.farmaciasperuanas.pmmli.materialinformation.repository;

import com.farmaciasperuanas.pmmli.materialinformation.entity.MaterialLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MaterialLotRepository extends JpaRepository<MaterialLot, Long>, JpaSpecificationExecutor<MaterialLot> {
    @Query("select ml from MaterialLot ml where ml.flagLi <> 1")
    List<MaterialLot> getListMaterialLot();

    @Modifying
    @Transactional
    @Query(value = "UPDATE SWLI.MATERIAL_LOT " +
            "SET FLAGLI = 1 " +
            "WHERE MATERIAL_CODE = :code and LOTE = :lot ", nativeQuery = true)
    void updateMaterialLot(@Param("code") String code,@Param("lot") String lot);
}
