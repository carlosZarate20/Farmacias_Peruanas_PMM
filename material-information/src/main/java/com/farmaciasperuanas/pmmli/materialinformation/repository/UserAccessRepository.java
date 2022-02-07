package com.farmaciasperuanas.pmmli.materialinformation.repository;

import com.farmaciasperuanas.pmmli.materialinformation.entity.UserAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccessRepository extends JpaRepository<UserAccess, Long>, JpaSpecificationExecutor<UserAccess> {
    @Query("SELECT u from UserAccess  u where u.username = :username")
    UserAccess findByUsername(@Param("username") String username);
}
