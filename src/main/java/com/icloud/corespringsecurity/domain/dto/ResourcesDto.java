package com.icloud.corespringsecurity.domain.dto;

import com.icloud.corespringsecurity.domain.entity.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourcesDto {

    private Long id;
    private String resourceName;
    private int orderNum;
    private ResourceType resourceType;
    private HttpMethod httpMethod;

    private String roleName;

    private List<String> roleList = new ArrayList<>();
}
