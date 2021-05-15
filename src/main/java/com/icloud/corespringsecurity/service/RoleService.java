package com.icloud.corespringsecurity.service;

import com.icloud.corespringsecurity.domain.dto.RoleDto;
import com.icloud.corespringsecurity.domain.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> getRoles();

    void createRole(RoleDto roleDto);

    Role getRole(Long id);

    void removeRole(Long id);
}
