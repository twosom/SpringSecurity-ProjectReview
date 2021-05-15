package com.icloud.corespringsecurity.domain.entity;


import lombok.*;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "roleList")
public class Resources {

    @Id @GeneratedValue
    @Column(name = "resources_id")
    private Long id;

    @Column(name = "resource_name")
    private String resourceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "http_method")
    private HttpMethod httpMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type")
    private ResourceType resourceType;

    @Column(name = "order_num")
    private int orderNum;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "resources_role",
            joinColumns = @JoinColumn(name = "resources_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roleList = new ArrayList<>();
}
