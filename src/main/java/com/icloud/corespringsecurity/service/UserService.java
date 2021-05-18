package com.icloud.corespringsecurity.service;

import com.icloud.corespringsecurity.domain.dto.AccountDto;
import com.icloud.corespringsecurity.domain.entity.Account;

import java.util.List;

public interface UserService {

    void createUser(AccountDto accountDto);

    List<Account> getAccounts();

    AccountDto getAccount(Long id);

    void modifyUser(AccountDto accountDto);

    void removeAccount(Long id);
}
