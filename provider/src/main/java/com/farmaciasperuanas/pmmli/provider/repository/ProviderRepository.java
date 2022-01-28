package com.farmaciasperuanas.pmmli.provider.repository;

import com.farmaciasperuanas.pmmli.provider.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long>, JpaSpecificationExecutor<Provider> {

    @Query("select p from Provider p where p.flagLi <> 1")
    public List<Provider> geListProvider();

    @Modifying
    @Transactional
    @Query(value = "UPDATE SWLI.PROVIDER " +
            "SET FLAGLI = 1 " +
            "WHERE CODE = :code", nativeQuery = true)
    void updateProvider(@Param("code") String code);

}
