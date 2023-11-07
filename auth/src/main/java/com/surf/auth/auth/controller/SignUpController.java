package com.surf.auth.auth.controller;

import com.surf.auth.auth.dto.SignUpDto;
import com.surf.auth.auth.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/signup")
    public ResponseEntity<String> SignUp(@RequestBody SignUpDto signupInfo) {
        return signUpService.saveUser(signupInfo);
    }
}