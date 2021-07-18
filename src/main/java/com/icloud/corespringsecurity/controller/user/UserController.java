package com.icloud.corespringsecurity.controller.user;

import com.icloud.corespringsecurity.domain.dto.AccountDto;
import com.icloud.corespringsecurity.repository.RoleRepository;
import com.icloud.corespringsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @GetMapping("/mypage")
    public String myPage() {

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
