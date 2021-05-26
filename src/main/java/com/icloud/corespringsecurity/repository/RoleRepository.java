package com.icloud.corespringsecurity.repository;

import com.icloud.corespringsecurity.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(
            "SELECT r " +
            "FROM Role r " +
            "ORDER BY r.roleGrade ASC ")
    List<Role> findAllOrderByRoleGrade();

    Role findByRoleName(String roleName);
}
