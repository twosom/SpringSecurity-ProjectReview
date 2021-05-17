package com.icloud.corespringsecurity.service.impl;

import com.icloud.corespringsecurity.domain.entity.RoleHierarchy;
import com.icloud.corespringsecurity.repository.RoleHierarchyRepository;
import com.icloud.corespringsecurity.service.RoleHierarchyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleHierarchyServiceImpl implements RoleHierarchyService {

    private final RoleHierarchyRepository roleHierarchyRepository;

    @Override
    public String findAllHierarchy() {

        StringBuilder sb = new StringBuilder();

        roleHierarchyRepository.findAll()
                .forEach(roleHierarchy -> {
                    if (roleHierarchy.getParentName() != null) {
                        sb.append(roleHierarchy.getParentName().getChildName());
                        sb.append(" > ");
                        sb.append(roleHierarchy.getChildName());
                        sb.append("\n");
                    }
                });

        return sb.toString();
    }
}
