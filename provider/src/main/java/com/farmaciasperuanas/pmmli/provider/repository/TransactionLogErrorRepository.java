package com.farmaciasperuanas.pmmli.provider.repository;

import com.farmaciasperuanas.pmmli.provider.dto.ErrorsDto;
import com.farmaciasperuanas.pmmli.provider.entity.TransactionLogError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionLogErrorRepository extends JpaRepository<TransactionLogError, Long>, JpaSpecificationExecutor<TransactionLogError> {
}
