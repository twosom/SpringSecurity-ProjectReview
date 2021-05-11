package com.icloud.corespringsecurity.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter @NoArgsConstructor
public class Account implements Serializable {

    @Id @GeneratedValue
    private Long id;

    private String username;

    private String password;

    private String email;

    private int age;

    private String role;

    @Builder
    public Account(String username, String password, String email, int age, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.age = age;
        this.role = role;
    }
}
