package com.icloud.corespringsecurity.domain.entity;

import lombok.*;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Resources implements Serializable {

    @Id @GeneratedValue
    @Column(name = "resources_id")
    private Long id;

    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "resource_type")
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    @Column(name = "http_method")
    @Enumerated(EnumType.STRING)
    private HttpMethod httpMethod;

    @Column(name = "order_num")
    private int orderNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

}
