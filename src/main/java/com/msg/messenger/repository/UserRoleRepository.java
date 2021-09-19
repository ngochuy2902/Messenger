package com.msg.messenger.repository;

import com.msg.messenger.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface UserRoleRepository
    extends JpaRepository<UserRole, Long> {

    @Query("SELECT new UserRole(ur.id, ur.userId, ur.roleId, r.name)"
        + " FROM UserRole ur"
        + " JOIN Role r ON r.id = ur.roleId"
        + " WHERE ur.userId = :userId")
    Set<UserRole> fetchByUserId(@Param("userId") Long userId);
}
