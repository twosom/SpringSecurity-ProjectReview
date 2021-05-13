package com.icloud.corespringsecurity.service.impl;

import com.icloud.corespringsecurity.domain.entity.Role;
import com.icloud.corespringsecurity.repository.RoleRepository;
import com.icloud.corespringsecurity.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServicesImpl implements RoleService {

    private final RoleRepository roleRepository;
    @Override
    public Role getRole(Long id) {
        return roleRepository.findById(id).orElse(new Role());
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void createRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
