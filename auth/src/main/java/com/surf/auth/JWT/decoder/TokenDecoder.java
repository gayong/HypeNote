package com.surf.auth.JWT.decoder;

import com.surf.auth.JWT.provider.SignKeyProvider;
import com.surf.auth.auth.dto.UserDto;
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

    public UserDto parsingRefreshToken(String refreshToken) {


        SecretKey secretKey = signKeyProvider.getSignKey();

        UserDto userInfo = new UserDto();

        Jws<Claims> userClaims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(refreshToken);

        Claims userClaim = userClaims.getPayload();

        int userPk = userClaim.get("userPk", int.class);
        String email = userClaim.get("email", String.class);
        String nickName = userClaim.get("nickName", String.class);
        String profileImage = userClaim.get("profileImage", String.class);
        String role = userClaim.get("role", String.class);

        userInfo.setUserPk(userPk);
        userInfo.setEmail(email);
        userInfo.setNickName(nickName);
        userInfo.setProfileImage(profileImage);
        userInfo.setRole(role);

        return userInfo;
    }

    public String parsingAccessToken(String accessToken) {

        SecretKey secretKey = signKeyProvider.getSignKey();

        Jws<Claims> userClaims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(accessToken);

        Claims userClaim = userClaims.getPayload();

        return userClaim.getSubject();
    }
}
