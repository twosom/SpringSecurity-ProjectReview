package com.icloud.corespringsecurity.domain.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class AccessIp implements Serializable {

    @Id @GeneratedValue
    @Column(name = "ip_id")
    private Long id;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;
}
