package com.icloud.corespringsecurity.repository;

import com.icloud.corespringsecurity.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String role);
}
