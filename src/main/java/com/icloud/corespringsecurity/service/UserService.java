package com.icloud.corespringsecurity.service;

import com.icloud.corespringsecurity.domain.dto.AccountDto;
import com.icloud.corespringsecurity.domain.entity.Account;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

public interface UserService {

    void createUser(AccountDto accountDto);

    void modifyUser(AccountDto accountDto);

    List<Account> getUsers();

    AccountDto getUser(Long id);

    void deleteUser(Long id);

    void order();
}
