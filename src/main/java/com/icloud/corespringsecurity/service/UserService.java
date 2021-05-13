package com.icloud.corespringsecurity.service;

import com.icloud.corespringsecurity.domain.dto.AccountDto;
import com.icloud.corespringsecurity.domain.entity.Account;

import java.util.List;

public interface UserService {

    void createUser(AccountDto accountDto);

    void modifyUser(AccountDto accountDto);

    List<Account> getUsers();

    AccountDto getUser(Long id);

    void deleteUser(Long id);
}
