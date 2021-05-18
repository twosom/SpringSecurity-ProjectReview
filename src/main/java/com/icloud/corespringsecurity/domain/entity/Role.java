package com.icloud.corespringsecurity.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Role implements Serializable {

    @Id @GeneratedValue
    @Column(name = "role_id")
    Long id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_desc")
    private String roleDesc;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_grade")
    private RoleGrade roleGrade;


}
