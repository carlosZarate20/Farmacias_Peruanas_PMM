package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.repository;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.materialinformation.materialinformation.entity.Barcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BarcodeRepository extends JpaRepository<Barcode, Long>, JpaSpecificationExecutor<Barcode> {
    @Query("select ba from Barcode ba where ba.flagLi <> 1")
    List<Barcode> getListBarcode();

    @Modifying
    @Transactional
    @Query(value = "UPDATE SWLI.BARCODE " +
            "SET FLAGLI = 1 " +
            "WHERE MATERIAL_CODE = :code", nativeQuery = true)
    void updateBarcode(@Param("code") String code);
}
