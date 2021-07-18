package com.icloud.corespringsecurity.service.impl;

import com.icloud.corespringsecurity.repository.RoleHierarchyRepository;
import com.icloud.corespringsecurity.service.RoleHierarchyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleHierarchyServiceImpl implements RoleHierarchyService {

    private final RoleHierarchyRepository roleHierarchyRepository;

    @Override
    public String findAllHierarchy() {

        StringBuilder concatedRoles = new StringBuilder();

        roleHierarchyRepository.findAll()
                .forEach(roleHierarchy -> {
                    if (roleHierarchy.getParentName() != null) {
                        concatedRoles.append(roleHierarchy.getParentName().getChildName());
                        concatedRoles.append(" > ");
                        concatedRoles.append(roleHierarchy.getChildName());
                        concatedRoles.append("\n");
                    }
                });

        return concatedRoles.toString();
    }

}
