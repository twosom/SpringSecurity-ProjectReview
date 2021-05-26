package com.icloud.corespringsecurity.service;

import com.icloud.corespringsecurity.domain.dto.RoleDto;
import com.icloud.corespringsecurity.domain.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> getRolesOrderByGrade();

    void createRole(RoleDto roleDto);

    RoleDto getRoleDtoById(Long id);

    void removeRole(Long id);

    void reloadRoleHierarchy();
}
