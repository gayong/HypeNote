package com.surf.auth.JWT.issuer;

import com.surf.auth.JWT.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AccessTokenIssuer {

    private final TokenProvider tokenProvider;

    @Value("${jwt.access-token-expiration-time}")
    private long EXPIRATION_TIME;

    public String accessTokenIssue(String email) {
        Map<String, Object> claims = new HashMap<>();
        return tokenProvider.createToken(claims, email, EXPIRATION_TIME);
    }
}