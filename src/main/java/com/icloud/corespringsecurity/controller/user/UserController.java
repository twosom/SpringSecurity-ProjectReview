package com.icloud.corespringsecurity.controller.user;

import com.icloud.corespringsecurity.domain.dto.AccountDto;
import com.icloud.corespringsecurity.domain.entity.Account;
import com.icloud.corespringsecurity.repository.RoleRepository;
import com.icloud.corespringsecurity.security.token.AjaxAuthenticationToken;
import com.icloud.corespringsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal Account account, Authentication authentication, Principal principal) {


        String username1 = account.getUsername();
        System.out.println("username1 = " + username1);

        Account account2 = (Account) authentication.getPrincipal();
        String username2 = account2.getUsername();
        System.out.println("username2 = " + username2);

        Account account3 = null;
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            account3 = (Account) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        } else if (principal instanceof AjaxAuthenticationToken) {
            account3 = (Account) ((AjaxAuthenticationToken) principal).getPrincipal();
        }

        String username3 = account3.getUsername();
        System.out.println("username3 = " + username3);

        Account account4 = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username4 = account4.getUsername();
        System.out.println("username4 = " + username4);

        return "user/mypage";

    }

    @GetMapping("/users")
    public String createUser() {
        return "user/login/register";
    }

    @PostMapping("/users")
    @ResponseBody
    public ResponseEntity createUser(@ModelAttribute AccountDto accountDto) {

        try {
            userService.createUser(accountDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Exception("잘못된 요청입니다."));
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
