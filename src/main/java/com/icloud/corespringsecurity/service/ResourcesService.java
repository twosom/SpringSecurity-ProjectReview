package com.icloud.corespringsecurity.service;

import com.icloud.corespringsecurity.domain.dto.ResourcesDto;
import com.icloud.corespringsecurity.domain.entity.Resources;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;
import java.util.List;

public interface ResourcesService {
    List<Resources> getResources();

    void createResources(ResourcesDto resourcesDto);

    void removeResources(Long id);

    ResourcesDto getResources(Long id);

    LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceMap();
}
