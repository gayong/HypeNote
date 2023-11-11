package com.surf.auth.JWT.decoder;

import com.surf.auth.JWT.provider.SignKeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class TokenDecoder {

    private final SignKeyProvider signKeyProvider;

    public String parsingRefreshToken(String refreshToken) {


        SecretKey secretKey = signKeyProvider.getSignKey();

        Jws<Claims> userClaims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(refreshToken);

        Claims userClaim = userClaims.getPayload();

        return userClaim.getSubject();
    }

    public String parsingAccessToken(String accessToken) {

        SecretKey secretKey = signKeyProvider.getSignKey();

        Jws<Claims> userClaims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(accessToken);

        Claims userClaim = userClaims.getPayload();

        return userClaim.getSubject();
    }
}
