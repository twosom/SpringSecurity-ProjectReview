package com.icloud.corespringsecurity.repository;

import com.icloud.corespringsecurity.domain.entity.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {


    Resources findByResourceNameAndHttpMethod(String resourcesName, HttpMethod httpMethod);
}
