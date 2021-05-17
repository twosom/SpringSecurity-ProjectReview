package com.icloud.corespringsecurity.security.listener;

import com.icloud.corespringsecurity.domain.entity.*;
import com.icloud.corespringsecurity.repository.ResourcesRepository;
import com.icloud.corespringsecurity.repository.RoleHierarchyRepository;
import com.icloud.corespringsecurity.repository.RoleRepository;
import com.icloud.corespringsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private final PasswordEncoder encoder;

    private static AtomicInteger count = new AtomicInteger(0);


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        setupSecurityResource();

        alreadySetup = true;
    }

    private void setupSecurityResource() {
        Role role_admin = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        Role role_user = createRoleIfNotFound("ROLE_USER", "유저");
        Role role_manager = createRoleIfNotFound("ROLE_MANAGER", "매니저");

        createResourceIfNotFound("/admin/**", HttpMethod.GET, role_admin, ResourceType.URL);
        createResourceIfNotFound("/config", HttpMethod.GET, role_admin, ResourceType.URL);

        createResourceIfNotFound("/messages", HttpMethod.GET, role_manager, ResourceType.URL);
        createResourceIfNotFound("/api/messages", HttpMethod.POST, role_manager, ResourceType.URL);

        createResourceIfNotFound("/mypage", HttpMethod.GET, role_user, ResourceType.URL);


        ArrayList<Role> roleList = new ArrayList<>();
        roleList.add(role_admin);
        roleList.add(role_manager);
        roleList.add(role_user);

        createUserIfNotFound("admin", "1111", "admin@admin.com", 11, roleList);

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

    public Resources createResourceIfNotFound(String resourcesName, HttpMethod httpMethod, Role role, ResourceType resourceType) {
        Resources resources = resourcesRepository.findByResourceNameAndHttpMethod(resourcesName, httpMethod);

        if (resources == null) {
            resources = Resources.builder()
                    .resourceName(resourcesName)
                    .role(role)
                    .httpMethod(httpMethod)
                    .resourceType(resourceType)
                    .orderNum(count.incrementAndGet())
                    .build();
        }

        return resourcesRepository.save(resources);
    }

    @Transactional
    public Account createUserIfNotFound(String username, String password, String email, int age, List<Role> roleList) {
        Account account = userRepository.findByUsername(username);
        if (account == null) {
            account = Account.builder()
                    .username(username)
                    .email(email)
                    .age(age)
                    .password(encoder.encode(password))
                    .roleList(roleList)
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
