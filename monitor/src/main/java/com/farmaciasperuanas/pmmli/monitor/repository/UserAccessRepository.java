package com.farmaciasperuanas.pmmli.monitor.repository;

import com.farmaciasperuanas.pmmli.monitor.dto.user.UserListItemDto;
import com.farmaciasperuanas.pmmli.monitor.entity.TransactionLog;
import com.farmaciasperuanas.pmmli.monitor.entity.UserAccess;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccessRepository extends JpaRepository<UserAccess, Long>, JpaSpecificationExecutor<UserAccess> {
    UserAccess findByUsername(String username);

    @Query("SELECT NEW com.farmaciasperuanas.pmmli.monitor.dto.user.UserListItemDto(ua.idUserAccess,ua.name,ua.username,ua.state,pu.name) from UserAccess  ua" +
            " inner join ua.profileUser pu " +
            " where (:s is null or :s = '' or ua.name like %:s% or ua.username like %:s%) and (:pro = 0L or pu.idProfileUser = :pro) and pu.idProfileUser <> 3")
    List<UserListItemDto> getUsers(@Param("s") String s,@Param("pro") Long pro, Pageable pageable);

    @Query("SELECT COUNT(ua.idUserAccess) from UserAccess  ua" +
            " inner join ua.profileUser pu " +
            " where (:s is null or :s = '' or ua.name like %:s% or ua.username like %:s%) and (:pro = 0L or pu.idProfileUser = :pro) and pu.idProfileUser <> 3")
    Integer countGetUsers(@Param("s") String s,@Param("pro") Long pro );
}
