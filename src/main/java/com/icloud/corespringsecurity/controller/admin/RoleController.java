package com.icloud.corespringsecurity.controller.admin;

import com.icloud.corespringsecurity.domain.dto.RoleDto;
import com.icloud.corespringsecurity.domain.entity.Role;
import com.icloud.corespringsecurity.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final ModelMapper mapper;

    @GetMapping("/admin/roles")
    public String getRoles(Model model) {
        List<Role> roles = roleService.getRoles();
        model.addAttribute("roles", roles);

        return "admin/role/list";
    }

    @GetMapping("/admin/roles/register")
    public String viewRoles(Model model) {
        RoleDto roleDto = new RoleDto();
        model.addAttribute("role", roleDto);

        return "admin/role/detail";
    }

    @PostMapping("/admin/roles")
    public String createRole(RoleDto roleDto) {
        Role role = mapper.map(roleDto, Role.class);
        roleService.createRole(role);

        return "redirect:/admin/roles";
    }

    @GetMapping("/admin/roles/{id}")
    public String getRole(@PathVariable("id") Long id, Model model) {
        Role role = roleService.getRole(id);

        RoleDto roleDto = mapper.map(role, RoleDto.class);
        model.addAttribute("role", roleDto);

        return "admin/role/detail";
    }

    @GetMapping("/admin/roles/delete/{id}")
    public String removeResources(@PathVariable("id") Long id, Model model) {
        roleService.deleteRole(id);
        return "redirect:/admin/resources";
    }
}
