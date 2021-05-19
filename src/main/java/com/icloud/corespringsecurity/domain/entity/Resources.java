package com.icloud.corespringsecurity.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resources implements Serializable {

    @Id @GeneratedValue
    @Column(name = "resource_id")
    private Long id;

    @Column(name = "reosurce_name")
    private String resourceName;

    @Column(name = "reosurce_type")
    private ResourceType resourceType;

    @Column(name = "http_method")
    private HttpMethod httpMethod;

    @Column(name = "order_num")
    private int orderNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
}
