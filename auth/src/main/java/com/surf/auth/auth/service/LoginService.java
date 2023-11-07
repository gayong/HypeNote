package com.surf.auth.auth.service;


import com.surf.auth.JWT.service.AccessTokenIssueService;
import com.surf.auth.JWT.service.RefreshTokenIssueService;
import com.surf.auth.auth.dto.LogInDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final AccessTokenIssueService accessTokenIssueService;
    private final RefreshTokenIssueService refreshTokenIssueService;
    private final AuthenticationManager authenticationManager;

    public boolean authentication (LogInDto loginInfo) {
//        String accessToken = accessTokenIssueService.accessTokenIssue();
//        String refreshToken = refreshTokenIssueService.refreshTokenIssue();
//        authenticationManager.authenticate(loginInfo.getEmail(), loginInfo.getPassword());


        return true;

    }
}
