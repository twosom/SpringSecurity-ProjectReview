package com.icloud.corespringsecurity.security.listener;

import com.icloud.corespringsecurity.domain.entity.*;
import com.icloud.corespringsecurity.repository.AccessIpRepository;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class DataInit implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ResourcesRepository resourcesRepository;
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
        Role role_admin = createRoleIfNotFound("ROLE_ADMIN", "관리자", RoleGrade.A);
        Role role_user = createRoleIfNotFound("ROLE_USER", "유저", RoleGrade.C);
        Role role_manager = createRoleIfNotFound("ROLE_MANAGER", "매니저", RoleGrade.B);

        createResourceIfNotFound("/admin/**", HttpMethod.GET, role_admin, ResourceType.URL);
        createResourceIfNotFound("/config", HttpMethod.GET, role_admin, ResourceType.URL);


        createResourceIfNotFound("/messages", HttpMethod.GET, role_manager, ResourceType.URL);
        createResourceIfNotFound("/api/messages", HttpMethod.POST, role_manager, ResourceType.URL);


        createResourceIfNotFound("/mypage", HttpMethod.GET, role_user, ResourceType.URL);




        createUserIfNotFound("admin", "1111", "admin@admin.com", 11, Arrays.asList(role_admin));
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
    public Role createRoleIfNotFound(String roleName, String roleDesc, RoleGrade roleGrade) {
        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .roleGrade(roleGrade)
                    .build();
        }

        return roleRepository.save(role);
    }
}
