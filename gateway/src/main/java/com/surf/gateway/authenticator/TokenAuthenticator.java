package com.surf.gateway.authenticator;

import com.surf.gateway.provider.JwtSecretKeyProvider;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenAuthenticator {

    private final JwtSecretKeyProvider jwtSecretKeyProvider;

    public boolean validateToken(String accessToken) {
        try {
            Jwts.parser().verifyWith(jwtSecretKeyProvider.getSignKey()).build().parseSignedClaims(accessToken);

            return true;

        } catch (JwtException e) {
            return false;
        }
    }
}
