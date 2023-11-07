package com.surf.auth.JWT.provider;

import com.surf.auth.auth.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class TokenProvider {

    private final Date DATE = new Date(System.currentTimeMillis());

    @Value("${jwt.secret}")
    private String SECRET;

    public String createToken(Map<String, Object> claims, User userInfo, long expirationTime) {

        int userId = userInfo.getUserId();
        String email = userInfo.getEmail();
        String nickName = userInfo.getNickName();
        String profileImage = userInfo.getProfileImage();
        List<String> documentsRoots = userInfo.getDocumentsRoots();
        String role = userInfo.getRole();

        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("nickName", nickName);
        claims.put("profileImage", profileImage);
        claims.put("documentsRoots", documentsRoots);
        claims.put("role", role);

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(DATE)
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey())
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
