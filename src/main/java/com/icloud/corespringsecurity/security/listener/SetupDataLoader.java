package com.icloud.corespringsecurity.security.listener;

import com.icloud.corespringsecurity.domain.entity.*;
import com.icloud.corespringsecurity.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ResourcesRepository resourcesRepository;
    private final RoleHierarchyRepository roleHierarchyRepository;
    private final AccessIpRepository accessIpRepository;

    private final PasswordEncoder encoder;

    private static AtomicInteger count = new AtomicInteger(0);


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        setupSecurityResource();
        setupAccessIpData("0:0:0:0:0:0:0:1");
        setupAccessIpData("127.0.0.1");

        alreadySetup = true;
    }

    private void setupAccessIpData(String ipAddress) {
        AccessIp byIpAddress = accessIpRepository.findByIpAddress(ipAddress);
        if (byIpAddress == null) {
            byIpAddress = AccessIp.builder()
                    .ipAddress(ipAddress)
                    .build();

            accessIpRepository.save(byIpAddress);
        }
    }

    private void setupSecurityResource() {
        Role role_admin = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        Role role_user = createRoleIfNotFound("ROLE_USER", "유저");
        Role role_manager = createRoleIfNotFound("ROLE_MANAGER", "매니저");
        HashSet<Role> adminSet = new HashSet<>();
        adminSet.add(role_admin);
        createResourceIfNotFound("/admin/**", "GET", adminSet, "url");
        createResourceIfNotFound("/config", "GET", adminSet, "url");

        HashSet<Role> managerSet = new HashSet<>();
        managerSet.add(role_manager);
        createResourceIfNotFound("/messages", "GET", managerSet, "url");
        createResourceIfNotFound("/api/messages", "POST", managerSet, "url");

        HashSet<Role> userSet = new HashSet<>();
        userSet.add(role_user);
        createResourceIfNotFound("/mypage", "GET", userSet, "url");


        HashSet<Role> allRoles = new HashSet<>();
        allRoles.add(role_admin);
        allRoles.add(role_manager);
        allRoles.add(role_user);

        createUserIfNotFound("admin", "1111", "admin@admin.com", 11, allRoles);

        createRoleHierarchyIfNotFound(role_manager, role_admin);
        createRoleHierarchyIfNotFound(role_user, role_manager);
    }

    @Transactional
    public void createRoleHierarchyIfNotFound(Role childRole, Role parentRole) {
        RoleHierarchy roleHierarchy = roleHierarchyRepository.findByChildName(parentRole.getRoleName());

        if (roleHierarchy == null) {
            roleHierarchy = RoleHierarchy.builder()
                    .childName(parentRole.getRoleName())
                    .build();
        }

        RoleHierarchy parentRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);

        roleHierarchy = roleHierarchyRepository.findByChildName(childRole.getRoleName());

        if (roleHierarchy == null) {
            roleHierarchy = RoleHierarchy.builder()
                    .childName(childRole.getRoleName())
                    .build();
        }

        RoleHierarchy childRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);

        childRoleHierarchy.setParentName(parentRoleHierarchy);


    }

    public Resources createResourceIfNotFound(String resourcesName, String httpMethod, Set<Role> roleSet, String resourceType) {
        Resources resources = resourcesRepository.findByResourceNameAndHttpMethod(resourcesName, httpMethod);

        if (resources == null) {
            resources = Resources.builder()
                    .resourceName(resourcesName)
                    .roleSet(roleSet)
                    .httpMethod(httpMethod)
                    .resourceType(resourceType)
                    .orderNum(count.incrementAndGet())
                    .build();
        }

        return resourcesRepository.save(resources);
    }

    @Transactional
    public Account createUserIfNotFound(String username, String password, String email, int age, Set<Role> roleSet) {
        Account account = userRepository.findByUsername(username);
        if (account == null) {
            account = Account.builder()
                    .username(username)
                    .email(email)
                    .age(age)
                    .password(encoder.encode(password))
                    .userRoles(roleSet)
                    .build();
        }

        return userRepository.save(account);
    }

    @Transactional
    public Role createRoleIfNotFound(String roleName, String roleDesc) {
        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .build();
        }

        return roleRepository.save(role);
    }
}
