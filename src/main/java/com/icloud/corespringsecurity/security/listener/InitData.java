package com.icloud.corespringsecurity.security.listener;

import com.icloud.corespringsecurity.domain.entity.Account;
import com.icloud.corespringsecurity.domain.entity.ResourceType;
import com.icloud.corespringsecurity.domain.entity.Resources;
import com.icloud.corespringsecurity.domain.entity.Role;
import com.icloud.corespringsecurity.repository.ResourcesRepository;
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
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class InitData implements ApplicationListener<ContextRefreshedEvent> {

    public boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ResourcesRepository resourcesRepository;

    private final PasswordEncoder encoder;


    private static AtomicInteger orderNum = new AtomicInteger(0);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        createSecurityResources();
    }

    private void createSecurityResources() {

        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        Role managerRole = createRoleIfNotFound("ROLE_USER", "유저");
        Role userRole = createRoleIfNotFound("ROLE_MANAGER", "매니저");

        ArrayList<Role> roleList = new ArrayList<>();
        roleList.add(adminRole);
        roleList.add(managerRole);
        roleList.add(userRole);

        createUserIfNotFound("admin", "1111", "admin@admin.com", 11, roleList);


        createResourcesIfNotFound("/mypage", HttpMethod.GET, roleList, ResourceType.URL);
        createResourcesIfNotFound("/messages", HttpMethod.GET, roleList, ResourceType.URL);
    }

    @Transactional
    public Resources createResourcesIfNotFound(String resourceName, HttpMethod httpMethod, ArrayList<Role> roleList, ResourceType resourceType) {
        Resources resources = resourcesRepository.findByResourceNameAndHttpMethod(resourceName, httpMethod);

        if (resources == null) {
            resources = Resources.builder()
                    .resourceName(resourceName)
                    .httpMethod(httpMethod)
                    .roleList(roleList)
                    .resourceType(resourceType)
                    .orderNum(orderNum.incrementAndGet())
                    .build();
        }

        return resourcesRepository.save(resources);
    }

    @Transactional
    public Account createUserIfNotFound(String username, String password, String email, int age, ArrayList<Role> roleList) {
        Account account = userRepository.findByUsername(username);
        if (account == null) {
            account = Account.builder()
                    .username(username)
                    .password(encoder.encode(password))
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
