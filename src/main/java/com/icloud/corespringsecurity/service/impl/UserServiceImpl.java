package com.icloud.corespringsecurity.service.impl;

import com.icloud.corespringsecurity.domain.Account;
import com.icloud.corespringsecurity.domain.AccountDto;
import com.icloud.corespringsecurity.repository.UserRepository;
import com.icloud.corespringsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public void createUser(AccountDto accountDto) {
        accountDto.setPassword(encoder.encode(accountDto.getPassword()));
        Account account = accountDto.toEntity();

        userRepository.save(account);
    }
}
