package com.icloud.corespringsecurity.security.listener;

import com.icloud.corespringsecurity.domain.entity.Account;
import com.icloud.corespringsecurity.domain.entity.Role;
import com.icloud.corespringsecurity.domain.entity.RoleGrade;
import com.icloud.corespringsecurity.repository.ResourcesRepository;
import com.icloud.corespringsecurity.repository.RoleRepository;
import com.icloud.corespringsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class DataInit implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ResourcesRepository repository;

    private final PasswordEncoder passwordEncoder;

    public boolean alreadySetup = false;

    private static final AtomicInteger orderNum = new AtomicInteger(0);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!alreadySetup) {
            createSecurityResources();
            this.alreadySetup = true;
        }

    }

    private void createSecurityResources() {
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자", RoleGrade.A);
        Role userRole = createRoleIfNotFound("ROLE_USER", "유저", RoleGrade.A);

        createAccountIfNotFound("admin", "1111", "admin@admin.com", 11, adminRole);

    }

    @Transactional
    public Account createAccountIfNotFound(String username, String password, String email, int age, Role role) {
        Account account = userRepository.findByUsername(username);
        if (account == null) {
            account = Account
                    .builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .age(age)
                    .roleList(Arrays.asList(role))
                    .build();
        }

        return userRepository.save(account);
    }

    @Transactional
    public Role createRoleIfNotFound(String roleName, String roleDesc, RoleGrade roleGrade) {
        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            role = Role
                    .builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .roleGrade(roleGrade)
                    .build();
        }

        return roleRepository.save(role);
    }
}
