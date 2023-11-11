package com.surf.auth.JWT.provider;

import com.surf.auth.auth.dto.UserDto;
import com.surf.auth.auth.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
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
