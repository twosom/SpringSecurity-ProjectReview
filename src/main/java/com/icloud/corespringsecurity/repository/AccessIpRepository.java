package com.icloud.corespringsecurity.repository;

import com.icloud.corespringsecurity.domain.entity.AccessIp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessIpRepository extends JpaRepository<AccessIp, Long> {
    AccessIp findByIpAddress(String ipAddress);
}
