package com.icloud.corespringsecurity.service.impl;

import com.icloud.corespringsecurity.domain.dto.RoleDto;
import com.icloud.corespringsecurity.domain.entity.Role;
import com.icloud.corespringsecurity.repository.RoleRepository;
import com.icloud.corespringsecurity.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper mapper;

    private final RoleHierarchyImpl roleHierarchy;

    @Override
    public List<Role> getRolesOrderByGrade() {
        return roleRepository.findAllOrderByRoleGrade();
    }

    @Override
    public void createRole(RoleDto roleDto) {
        Role role = mapper.map(roleDto, Role.class);
        roleRepository.save(role);
    }

    @Override
    public RoleDto getRole(Long id) {
        Role role = roleRepository.findById(id).orElse(new Role());
        return mapper.map(role, RoleDto.class);
    }

    @Override
    public void removeRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public void reloadRoleHierarchy() {
        List<Role> allRoles = roleRepository.findAllOrderByRoleGrade();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < allRoles.size(); i++) {
            try {
                allRoles.get(i + 1); /* 검증 코드 */
                sb.append(allRoles.get(i).getRoleName());
                sb.append(" > ");
                sb.append(allRoles.get(i + 1).getRoleName());
                sb.append("\n");
            } catch (Exception e) {
                break;
            }
        }

        roleHierarchy.setHierarchy(sb.toString());
    }
}
