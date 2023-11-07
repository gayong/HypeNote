package com.surf.auth.auth.controller;

import com.surf.auth.JWT.service.AccessTokenIssueService;
import com.surf.auth.JWT.service.RefreshTokenIssueService;
import com.surf.auth.auth.dto.AuthenticationResultDto;
import com.surf.auth.auth.dto.LogInDto;
import com.surf.auth.auth.dto.TokenDto;
import com.surf.auth.auth.entity.User;
import com.surf.auth.auth.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> LogIn(@RequestBody LogInDto loginInfo, HttpServletResponse response) {
        TokenDto fail = new TokenDto();

        AuthenticationResultDto authenticationResultDto = loginService.authentication(loginInfo);
        if (authenticationResultDto.isResult()) {
            User userInfo = authenticationResultDto.getUserInfo();
            return ResponseEntity.ok(loginService.sendToken(userInfo, response));
        }
        fail.setRefreshToken(null);
        fail.setAccessToken(null);
        fail.setMessage("로그인 정보가 일치하지 않습니다.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(fail);
    }
}