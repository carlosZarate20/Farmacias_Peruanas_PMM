package com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.localstore.localstore.repository;

import com.farmaciasperuanas.digital.com.farmaciasperuanas.pmmli.localstore.localstore.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, JpaSpecificationExecutor<Store> {
    @Query("select s from Store s")
    List<Store> getListStore();
}
