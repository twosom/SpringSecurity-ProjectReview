package com.icloud.corespringsecurity.controller.admin;

import com.icloud.corespringsecurity.domain.dto.AccountDto;
import com.icloud.corespringsecurity.domain.entity.Account;
import com.icloud.corespringsecurity.service.RoleService;
import com.icloud.corespringsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserManagerController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/admin/accounts")
    public String allAccounts(Model model) {
        List<Account> accountList = userService.getAccounts();
        model.addAttribute("accountList", accountList);

        return "admin/user/list";
    }

    @GetMapping("/admin/accounts/{id}")
    public String accountDetail(@PathVariable("id") Long id, Model model) {
        AccountDto accountDto = userService.getAccount(id);
        model.addAttribute("account", accountDto);
        model.addAttribute("roleList", roleService.getRoles());

        return "admin/user/detail";
    }

    @PostMapping("/admin/accounts")
    public String modifyUser(AccountDto accountDto) {
        userService.modifyUser(accountDto);

        return "redirect:/admin/accounts";
    }

    @GetMapping("/admin/accounts/delete/{id}")
    public String removeAccount(@PathVariable("id") Long id) {
        userService.removeAccount(id);

        return "redirect:/admin/accounts";
    }
}
