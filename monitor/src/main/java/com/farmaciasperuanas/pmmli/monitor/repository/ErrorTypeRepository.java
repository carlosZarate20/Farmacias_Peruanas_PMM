package com.farmaciasperuanas.pmmli.monitor.repository;

import com.farmaciasperuanas.pmmli.monitor.entity.ErrorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErrorTypeRepository extends JpaRepository<ErrorType, Long>, JpaSpecificationExecutor<ErrorType> {

    @Query("select E from ErrorType E")
    List<ErrorType> getErrorType();
}
