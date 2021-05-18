package com.icloud.corespringsecurity.repository;

import com.icloud.corespringsecurity.domain.entity.Resources;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {
}
