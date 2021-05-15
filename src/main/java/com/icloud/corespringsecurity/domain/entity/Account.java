package com.icloud.corespringsecurity.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "roleList")
public class Account implements Serializable {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String username;

    private String password;

    private String email;

    private int age;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roleList = new ArrayList<>();
}
