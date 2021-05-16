package com.icloud.corespringsecurity.security.listener;

import com.icloud.corespringsecurity.domain.entity.Account;
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

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class InitData implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ResourcesRepository resourcesRepository;

    private final PasswordEncoder passwordEncoder;

    public boolean alreadySetup = false;

    private static AtomicInteger orderNum = new AtomicInteger(0);


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        createSecurityResource();
    }

    private void createSecurityResource() {
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        Role userRole = createRoleIfNotFound("ROLE_USER", "유저");
        Role managerRole = createRoleIfNotFound("ROLE_MAANGER", "매니저");

        ArrayList<Role> roleList = new ArrayList<>();
        roleList.add(adminRole);
        roleList.add(userRole);
        roleList.add(managerRole);

        createUserIfNotFound("admin", "1111", "admin@admin.com", 11, roleList);
    }

    @Transactional
    public Account createUserIfNotFound(String username, String password, String email, int age, ArrayList<Role> roleList) {
        Account account = userRepository.findByUsername(username);
        if (account == null) {
            account = Account.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .age(age)
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
