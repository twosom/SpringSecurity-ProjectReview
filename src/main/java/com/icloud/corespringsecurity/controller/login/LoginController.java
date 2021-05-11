package com.icloud.corespringsecurity.controller.login;

import com.icloud.corespringsecurity.domain.Account;
import com.icloud.corespringsecurity.security.service.AccountContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "user/login/login";
    }

    @GetMapping("/logout")
    @ResponseBody
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !((Account)authentication.getPrincipal()).getRole().equals("ROLE_ANONYMOUS")) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("로그아웃에 성공하였습니다.");
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("잘못된 요청입니다.");
    }
}