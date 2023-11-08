package com.surf.auth.JWT.service;

import com.surf.auth.JWT.provider.TokenProvider;
import com.surf.auth.auth.dto.UserDto;
import com.surf.auth.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AccessTokenIssueService {

    private final TokenProvider tokenProvider;

    @Value("${jwt.access-token-expiration-time}")
    private long EXPIRATION_TIME;

    public String accessTokenIssue(UserDto userInfo) {
        Map<String, Object> claims = new HashMap<>();
        return tokenProvider.createToken(claims, userInfo, EXPIRATION_TIME);
    }
}