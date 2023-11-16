package com.surf.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestingController {

    @GetMapping("/api/member/testing")
    public String testing () {
        return "Server Testing 성공!";
    }
}
