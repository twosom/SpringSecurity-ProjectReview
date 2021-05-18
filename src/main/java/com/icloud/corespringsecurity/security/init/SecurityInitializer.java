package com.icloud.corespringsecurity.security.init;

import com.icloud.corespringsecurity.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityInitializer implements ApplicationRunner {

    private final RoleService roleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        roleService.reloadRoleHierarchy();
    }
}
