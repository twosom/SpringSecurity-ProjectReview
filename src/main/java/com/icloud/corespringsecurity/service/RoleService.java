package com.icloud.corespringsecurity.service;

import com.icloud.corespringsecurity.domain.entity.Role;

import java.util.List;

public interface RoleService {

    Role getRole(Long id);

    List<Role> getRoles();

    void createRole(Role role);

    void deleteRole(Long id);
}
