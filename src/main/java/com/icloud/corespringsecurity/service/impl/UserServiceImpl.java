package com.icloud.corespringsecurity.service.impl;

import com.icloud.corespringsecurity.domain.entity.Account;
import com.icloud.corespringsecurity.domain.dto.AccountDto;
import com.icloud.corespringsecurity.domain.entity.Role;
import com.icloud.corespringsecurity.repository.RoleRepository;
import com.icloud.corespringsecurity.repository.UserRepository;
import com.icloud.corespringsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        accountDto.setPassword(encoder.encode(accountDto.getPassword()));
        Role role_user = roleRepository.findByRoleName("ROLE_USER");

        Account account = mapper.map(accountDto, Account.class);
        account.getRoleList().add(role_user);

        userRepository.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return userRepository.findAll();
    }

    @Override
    public AccountDto getAccount(Long id) {
        Account account = userRepository.findById(id).orElse(new Account());
        AccountDto accountDto = mapper.map(account, AccountDto.class);


        if (account.getRoleList() != null) {
            List<String> roleList = account.getRoleList()
                    .stream().map(Role::getRoleName)
                    .collect(Collectors.toList());
            accountDto.setRoleList(roleList);
        }

        return accountDto;
    }

    @Override
    public void modifyAccount(AccountDto accountDto) {
        accountDto.setPassword(encoder.encode(accountDto.getPassword()));
        Account account = mapper.map(accountDto, Account.class);

        if (accountDto.getRoleList() != null) {
            List<Role> roleList = accountDto.getRoleList()
                    .stream().map(roleRepository::findByRoleName)
                    .collect(Collectors.toList());

            account.setRoleList(roleList);
        }

        userRepository.save(account);
    }

    @Override
    public void removeAccount(Long id) {
        userRepository.deleteById(id);
    }
}
