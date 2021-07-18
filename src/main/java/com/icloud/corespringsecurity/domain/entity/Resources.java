package com.icloud.corespringsecurity.domain.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"roleSet"})
@EqualsAndHashCode(of = "id")
public class Resources {

    @Id @GeneratedValue
    @Column(name = "resource_id")
    private Long id;

    private String resourceName;

    private String httpMethod;

    private int orderNum;

    private String resourceType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_resources",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roleSet = new HashSet<>();



}
