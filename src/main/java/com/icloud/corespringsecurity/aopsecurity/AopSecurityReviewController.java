package com.icloud.corespringsecurity.aopsecurity;

import com.icloud.corespringsecurity.domain.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AopSecurityReviewController {

    @GetMapping("/preAuthorize")
    @PreAuthorize("hasRole('ROLE_USER') AND  #account.username == principal.username")
    public String preAuthorize(AccountDto account, Model model, Principal principal) {

        model.addAttribute("method", "Success @PreAuthorize");

        return "aop/method";
    }


}
