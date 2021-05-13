package com.icloud.corespringsecurity.service;

import com.icloud.corespringsecurity.domain.entity.Resources;

import java.util.List;

public interface ResourcesService {

    Resources getResources(Long id);

    List<Resources> getResources();

    void createResources(Resources resources);

    void deleteResources(Long id);
}
