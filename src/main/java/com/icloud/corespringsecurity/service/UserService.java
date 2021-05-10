package com.icloud.corespringsecurity.service;

import com.icloud.corespringsecurity.domain.AccountDto;

public interface UserService {

    void createUser(AccountDto accountDto);
}
