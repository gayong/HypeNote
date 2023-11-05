package com.surf.auth.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@AllArgsConstructor
@RequestMapping("/api/auth")
public class SignUpController {

    @PostMapping("/signup")
    public void SignUp() {

    }
}

