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
@ToString(exclude = "roleHierarchy")
@EqualsAndHashCode(of = "id")
public class RoleHierarchy implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "child_name")
    private String childName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_name", referencedColumnName = "child_name")
    private RoleHierarchy parentName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentName")
    private List<RoleHierarchy> roleHierarchy = new ArrayList<>();


}
