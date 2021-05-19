package com.icloud.corespringsecurity.domain.dto;

import com.icloud.corespringsecurity.domain.entity.RoleGrade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {

    private Long id;
    private String roleName;
    private String roleDesc;
    private RoleGrade roleGrade;
}
