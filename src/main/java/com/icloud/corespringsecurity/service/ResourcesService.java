package com.icloud.corespringsecurity.service;

import com.icloud.corespringsecurity.domain.entity.Resources;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;
import java.util.List;

public interface ResourcesService {

    Resources getResources(Long id);

    List<Resources> getResources();

    void createResources(Resources resources);

    void deleteResources(Long id);

    LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList();
}
