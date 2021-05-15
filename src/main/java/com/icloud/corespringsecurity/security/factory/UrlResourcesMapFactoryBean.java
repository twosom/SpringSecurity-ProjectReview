package com.icloud.corespringsecurity.security.factory;

import com.icloud.corespringsecurity.service.ResourcesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UrlResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap;

    private final ResourcesService resourcesService;

    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() throws Exception {
        if (requestMap == null) {
            requestMap = resourcesService.getResourcesMap();
        }

        return requestMap;
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
