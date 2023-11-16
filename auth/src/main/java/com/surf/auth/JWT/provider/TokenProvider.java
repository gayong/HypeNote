package com.surf.auth.JWT.provider;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class TokenProvider {

    private final SignKeyProvider signKeyProvider;

    private final Date DATE = new Date(System.currentTimeMillis());

    public String createToken(Map<String, Object> claims, String email, long expirationTime) {

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(DATE)
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(signKeyProvider.getSignKey())
                .compact();
    }
}
