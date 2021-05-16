package com.icloud.corespringsecurity.service.impl;

import com.icloud.corespringsecurity.domain.dto.RoleDto;
import com.icloud.corespringsecurity.domain.entity.Role;
import com.icloud.corespringsecurity.repository.RoleRepository;
import com.icloud.corespringsecurity.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final ModelMapper mapper;

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void createRole(RoleDto roleDto) {
        Role role = mapper.map(roleDto, Role.class);
        roleRepository.save(role);
    }

    @Override
    public RoleDto getRole(Long id) {
        return mapper.map(roleRepository.findById(id).orElse(new Role()), RoleDto.class);
    }

    @Override
    public void removeRole(Long id) {
        roleRepository.deleteById(id);
    }
}
