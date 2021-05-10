package com.icloud.corespringsecurity.domain;

import lombok.Data;

@Data
public class AccountDto {

    private String username;
    private String password;
    private String email;
    private int age;
    private String role;

    public Account toEntity() {
        return Account.builder()
                .username(getUsername())
                .password(getPassword())
                .email(getEmail())
                .age(getAge())
                .role(getRole())
                .build();
    }

}
