package com.surf.auth.auth.controller;

import com.surf.auth.auth.dto.rquest.SignUpDto;
import com.surf.auth.auth.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/signup")
    public ResponseEntity<String> SignUp(@ModelAttribute SignUpDto signupInfo) throws IOException {
        return signUpService.saveUser(signupInfo);
    }
}