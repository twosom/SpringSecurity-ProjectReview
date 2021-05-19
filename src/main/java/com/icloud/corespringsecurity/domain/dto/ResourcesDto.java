package com.icloud.corespringsecurity.domain.dto;

import com.icloud.corespringsecurity.domain.entity.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourcesDto {

    private Long id;
    private String resourceName;
    private ResourceType resourceType;
    private HttpMethod httpMethod;
    private int orderNum;
    private String role;

}
