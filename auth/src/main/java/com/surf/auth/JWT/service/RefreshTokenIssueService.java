package com.surf.auth.JWT.service;

import com.surf.auth.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class RefreshTokenIssueService {

    private final AccessTokenIssueService accessTokenIssueService;

    @Value("${jwt.refresh-token-expiration-time}")
    private long EXPIRATION_TIME;

    public String refreshTokenIssue (User userInfo) {
        Map<String, Object> claims = new HashMap<>();
        return accessTokenIssueService.createToken(claims, userInfo, EXPIRATION_TIME);
    }
}
