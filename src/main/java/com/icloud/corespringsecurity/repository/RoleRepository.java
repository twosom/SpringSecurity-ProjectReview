package com.icloud.corespringsecurity.repository;

import com.icloud.corespringsecurity.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String roleName);
}
