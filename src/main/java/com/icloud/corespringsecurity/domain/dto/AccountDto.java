package com.icloud.corespringsecurity.domain.dto;

import com.icloud.corespringsecurity.domain.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {

    private String id;
    private String username;
    private String password;
    private String email;
    private int age;
    private List<String> roles;

}
