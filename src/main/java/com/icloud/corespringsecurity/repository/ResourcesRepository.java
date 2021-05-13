package com.icloud.corespringsecurity.repository;

import com.icloud.corespringsecurity.domain.entity.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {

    @Query(
            "SELECT r " +
            "FROM Resources  r " +
            "JOIN FETCH r.roleSet " +
            "WHERE r.resourceType = 'url' " +
            "ORDER BY r.orderNum DESC "
    )
    List<Resources> findAllResources();


    @Query(
            "SELECT r " +
            "FROM Resources r " +
            "JOIN FETCH r.roleSet " +
            "WHERE r.resourceType = 'method' " +
            "ORDER BY r.orderNum DESC"
    )
    List<Resources> findAllMethodResources();

    @Query(
            "SELECT r " +
            "FROM Resources r " +
            "JOIN FETCH r.roleSet " +
            "WHERE r.resourceType = 'pointcut' " +
            "ORDER BY r.orderNum DESC"
    )
    List<Resources> findAllPointcutResources();

    Resources findByResourceNameAndHttpMethod(String resourceName, String httpMethod);

}
