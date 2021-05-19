package com.icloud.corespringsecurity.service.impl;

import com.icloud.corespringsecurity.domain.entity.AccessIp;
import com.icloud.corespringsecurity.repository.AccessIpRepository;
import com.icloud.corespringsecurity.service.AccessIpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AccessIpServiceImpl implements AccessIpService {

    private final AccessIpRepository accessIpRepository;


    @Override
    public List<String> getIpList() {
        List<AccessIp> allIp = accessIpRepository.findAll();

        return allIp.stream().map(AccessIp::getIpAddress)
                .collect(Collectors.toList());
    }
}
