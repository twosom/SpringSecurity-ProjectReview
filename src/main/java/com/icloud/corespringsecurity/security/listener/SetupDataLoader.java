package com.icloud.corespringsecurity.security.listener;

import com.icloud.corespringsecurity.domain.entity.Account;
import com.icloud.corespringsecurity.domain.entity.Resources;
import com.icloud.corespringsecurity.domain.entity.Role;
import com.icloud.corespringsecurity.repository.ResourcesRepository;
import com.icloud.corespringsecurity.repository.RoleRepository;
import com.icloud.corespringsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        Set<Role> roles = new HashSet<>();
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        roles.add(adminRole);
        createResourceIfNotFound("/admin/**", "", roles, "url");
        Account account = createUserIfNotFound("admin", "1111", "admin@gmail.com", 10, roles);
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
