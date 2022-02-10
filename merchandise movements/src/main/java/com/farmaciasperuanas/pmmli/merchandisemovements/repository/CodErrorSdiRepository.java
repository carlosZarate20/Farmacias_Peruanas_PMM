package com.farmaciasperuanas.pmmli.merchandisemovements.repository;

import com.farmaciasperuanas.pmmli.merchandisemovements.entity.CodErrorSdi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodErrorSdiRepository extends JpaRepository<CodErrorSdi, Long> {

    @Query("select cdi from CodErrorSdi cdi where cdi.sessionNumber = :sessionNumber and cdi.typeTransaction = :typeTransaction")
    List<CodErrorSdi> listError(@Param("sessionNumber") Integer sessionNumber, @Param("typeTransaction") String typeTransaction);
}
