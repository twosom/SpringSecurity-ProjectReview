package com.icloud.corespringsecurity.repository;

import com.icloud.corespringsecurity.domain.entity.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {

    @Query(
            "SELECT r " +
            "FROM Resources r " +
            "ORDER BY r.orderNum DESC")
    List<Resources> findAllOrderByOrderNumDesc();
}
