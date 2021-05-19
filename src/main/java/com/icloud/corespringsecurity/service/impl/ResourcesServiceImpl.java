package com.icloud.corespringsecurity.service.impl;

import com.icloud.corespringsecurity.domain.dto.ResourcesDto;
import com.icloud.corespringsecurity.domain.entity.Resources;
import com.icloud.corespringsecurity.domain.entity.Role;
import com.icloud.corespringsecurity.repository.ResourcesQueryDslRepository;
import com.icloud.corespringsecurity.repository.ResourcesRepository;
import com.icloud.corespringsecurity.repository.RoleRepository;
import com.icloud.corespringsecurity.service.ResourcesService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ResourcesServiceImpl implements ResourcesService {

    private final ResourcesRepository resourcesRepository;
    private final ResourcesQueryDslRepository resourcesQueryDslRepository;

    private final RoleRepository roleRepository;

    private final ModelMapper mapper;

    @Override
    public List<Resources> getResources() {
        return resourcesRepository.findAll();
    }

    @Override
    public void createResources(ResourcesDto resourcesDto) {
        Resources resources = mapper.map(resourcesDto, Resources.class);

        Role role = roleRepository.findByRoleName(resourcesDto.getRole());
        resources.setRole(role);

        resourcesRepository.save(resources);
    }

    @Override
    public ResourcesDto getResource(Long id) {
        Resources resources = resourcesRepository.findById(id).orElse(new Resources());
        ResourcesDto resourcesDto = mapper.map(resources, ResourcesDto.class);

        resourcesDto.setRole(resources.getRole().getRoleName());

        return resourcesDto;
    }

    @Override
    public void removeResources(Long id) {
        resourcesRepository.deleteById(id);
    }

    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourcesMap() {
        List<Resources> allResources = resourcesQueryDslRepository.getAllResources();
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        allResources.forEach(resources -> {

            result.put(new AntPathRequestMatcher(resources.getResourceName(), resources.getHttpMethod().name()),
                    Arrays.asList(new SecurityConfig(resources.getRole().getRoleName())));
        });

        return result;
    }

}
