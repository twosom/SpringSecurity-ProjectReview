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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return resourcesRepository.findAllOrderByOrderNumDesc();
    }

    @Override
    public void createResources(ResourcesDto resourcesDto) {
        Resources resources = mapper.map(resourcesDto, Resources.class);

        if (resourcesDto.getRoleList() != null) {
            List<Role> roleList = resourcesDto.getRoleList()
                    .stream().map(roleRepository::findByRoleName)
                    .collect(Collectors.toList());

            resources.setRoleList(roleList);
        }

        resourcesRepository.save(resources);
    }

    @Override
    public ResourcesDto getResources(Long id) {
        Resources resources = resourcesRepository.findById(id).orElse(new Resources());
        ResourcesDto resourcesDto = mapper.map(resources, ResourcesDto.class);

        if (resources.getRoleList() != null) {
            List<String> roleList = resources.getRoleList()
                    .stream().map(Role::getRoleName)
                    .collect(Collectors.toList());

            resourcesDto.setRoleList(roleList);
        }

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
            List<ConfigAttribute> configAttributeList = resources.getRoleList()
                    .stream().map(Role::getRoleName)
                    .map(SecurityConfig::new)
                    .collect(Collectors.toList());

            result.put(new AntPathRequestMatcher(resources.getResourceName(), resources.getHttpMethod().name()), configAttributeList);
        });
        return result;
    }
}
