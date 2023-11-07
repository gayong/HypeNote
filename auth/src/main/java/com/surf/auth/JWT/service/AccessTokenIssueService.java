package com.surf.auth.JWT.service;

import com.surf.auth.auth.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AccessTokenIssueService {

    private final Date date = new Date(System.currentTimeMillis());

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.access-token-expiration-time")
    private Long EXPIRATION_TIME;

    public String accessTokenIssue(User userInfo) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userInfo);
    }

    private String createToken(Map<String, Object> claims, User userInfo) {

        String email = userInfo.getEmail();
        String nickName = userInfo.getNickName();
        String profileImage = userInfo.getProfileImage();
        List<String> documentsRoots = userInfo.getDocumentsRoots();
        String role = userInfo.getRole();

        claims.put("email", email);
        claims.put("nickName", nickName);
        claims.put("profileImage", profileImage);
        claims.put("documentsRoots", documentsRoots);
        claims.put("role", role);

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(date)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey())
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}