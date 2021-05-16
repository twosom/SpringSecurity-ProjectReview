package com.icloud.corespringsecurity.controller.admin;

import com.icloud.corespringsecurity.domain.dto.RoleDto;
import com.icloud.corespringsecurity.domain.entity.Role;
import com.icloud.corespringsecurity.service.RoleService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/admin/roles")
    public String allRoles(Model model) {
        List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);

        return "admin/role/list";
    }

    @GetMapping("/admin/roles/register")
    public String createRoleForm(Model model) {
        model.addAttribute("role", new RoleDto());

        return "admin/role/detail";
    }


    @PostMapping("/admin/roles/register")
    public String createRole(RoleDto roleDto) {
        roleService.createRole(roleDto);

        return "redirect:/admin/roles";
    }

    @GetMapping("/admin/roles/{id}")
    public String roleDetail(@PathVariable("id") Long id, Model model) {
        RoleDto roleDto = roleService.getRole(id);
        model.addAttribute("role", roleDto);


        return "admin/role/detail";
    }

    @GetMapping("/admin/roles/delete/{id}")
    public String removeRole(@PathVariable("id") Long id) {
        roleService.removeRole(id);

        return "redirect:/admin/roles";
    }
}
