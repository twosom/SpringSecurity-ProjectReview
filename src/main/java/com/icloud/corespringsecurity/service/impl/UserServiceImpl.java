package com.icloud.corespringsecurity.service.impl;

import com.icloud.corespringsecurity.domain.dto.AccountDto;
import com.icloud.corespringsecurity.domain.entity.Account;
import com.icloud.corespringsecurity.domain.entity.Role;
import com.icloud.corespringsecurity.repository.RoleRepository;
import com.icloud.corespringsecurity.repository.UserRepository;
import com.icloud.corespringsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;
    private final ModelMapper mapper;

    @Override
    public void createUser(AccountDto accountDto) {
        Role role = roleRepository.findByRoleName("ROLE_USER");
        accountDto.setPassword(encoder.encode(accountDto.getPassword()));
        Account account = mapper.map(accountDto, Account.class);
        account.getUserRoles().add(role);
        userRepository.save(account);
    }

    @Override
    public void modifyUser(AccountDto accountDto) {
        Account account = mapper.map(accountDto, Account.class);

        if (accountDto.getRoles() != null) {
            Set<Role> roles = new HashSet<>();
            accountDto.getRoles().forEach(role -> {
                Role r = roleRepository.findByRoleName(role);
                roles.add(r);
            });
            account.setUserRoles(roles);
        }

        account.setPassword(encoder.encode(accountDto.getPassword()));
        userRepository.save(account);
    }

    @Override
    public List<Account> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public AccountDto getUser(Long id) {
        Account account = userRepository.findById(id)
                .orElse(new Account());

        AccountDto accountDto = mapper.map(account, AccountDto.class);
        List<String> roles = account.getUserRoles().stream().map(Role::getRoleName).collect(Collectors.toList());
        accountDto.setRoles(roles);

        return accountDto;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
