package com.surf.auth.security.controller;

import com.surf.auth.security.dto.SignUpDto;
import com.surf.auth.security.service.SignUpService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/signup")
    public String SignUp(@RequestBody SignUpDto userInfo) {
        return signUpService.saveUser(userInfo);
    }
}

