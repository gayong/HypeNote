package com.surf.auth.JWT.authenticator;

import com.surf.auth.JWT.provider.SignKeyProvider;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Slf4j
@RequiredArgsConstructor
@Component
public class AccessTokenAuthenticator {

    private final SignKeyProvider signKeyProvider;

    public boolean authenticateToken(String accessToken) {
        try {

            Jwts.parser().verifyWith(signKeyProvider.getSignKey()).build().parseSignedClaims(accessToken);

            return true;

        } catch (JwtException e) {
            log.info(e.getMessage());
            return false;
        }
    }
}
