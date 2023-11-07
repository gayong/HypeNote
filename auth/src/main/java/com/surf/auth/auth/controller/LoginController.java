package com.surf.auth.auth.controller;

import com.surf.auth.JWT.service.AccessTokenIssueService;
import com.surf.auth.JWT.service.RefreshTokenIssueService;
import com.surf.auth.auth.dto.LogInDto;
import com.surf.auth.auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;
    private final AccessTokenIssueService accessTokenIssueService;
    private final RefreshTokenIssueService refreshTokenIssueService;

    @PostMapping("/login")
    public String LogIn(@RequestBody LogInDto loginInfo) {
        if (loginService.authentication(loginInfo)) {

            accessTokenIssueService.accessTokenIssue();
            refreshTokenIssueService.refreshTokenIssue();

            return "나중에 여기다 토큰 정보 넣기";

        } else {
            return "로그인 정보가 없습니다.";
        }
    }
}