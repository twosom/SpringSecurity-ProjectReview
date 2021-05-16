package com.icloud.corespringsecurity.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resources {

    @Id @GeneratedValue
    @Column(name = "resources_id")
    private Long id;

    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "order_num")
    private int orderNum;

    @Column(name = "resource_type")
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    @Column(name = "http_method")
    @Enumerated(EnumType.STRING)
    private HttpMethod httpMethod;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "resources_role",
            joinColumns = @JoinColumn(name = "resources_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roleList = new ArrayList<>();
}
