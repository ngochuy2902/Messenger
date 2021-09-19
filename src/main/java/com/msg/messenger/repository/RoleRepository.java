package com.msg.messenger.repository;

import com.msg.messenger.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository
    extends JpaRepository<Role, Long> {
}
