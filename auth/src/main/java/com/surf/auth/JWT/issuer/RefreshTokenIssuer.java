package com.surf.auth.JWT.issuer;

import com.surf.auth.JWT.provider.TokenProvider;
import com.surf.auth.JWT.service.RedisRefreshTokenSaveService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class RefreshTokenIssuer {

    private final TokenProvider tokenProvider;
    private final RedisRefreshTokenSaveService redisRefreshTokenSaveService;

    @Value("${jwt.refresh-token-expiration-time}")
    private long EXPIRATION_TIME;

    @Value("${jwt.refresh-token-cookie-name}")
    private String REFRESH_TOKEN_COOKIE_NAME;

    @Value("${jwt.refresh-token-cookie-path}")
    private String REFRESH_TOKEN_COOKIE_PATH;

    @Value("${jwt.refresh-token-cookie-max-age}")
    private int REFRESH_TOKEN_COOKIE_MAX_AGE;

    public void refreshTokenIssue(String email, HttpServletResponse response) {
        Map<String, Object> claims = new HashMap<>();
        String refreshToken = tokenProvider.createToken(claims, email, EXPIRATION_TIME);

        ResponseCookie responseTokenCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .maxAge(REFRESH_TOKEN_COOKIE_MAX_AGE)
                .path(REFRESH_TOKEN_COOKIE_PATH)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .build();

        response.addHeader("Set-Cookie", responseTokenCookie.toString());

        redisRefreshTokenSaveService.saveRefreshToken(EXPIRATION_TIME, refreshToken, email);
    }
}
