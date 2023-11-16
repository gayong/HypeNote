package com.surf.auth.JWT.service;

import com.surf.auth.JWT.issuer.AccessTokenIssuer;
import com.surf.auth.auth.dto.response.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ReissueService {

    private final StringRedisTemplate redisTemplate;
    private final AccessTokenIssuer accessTokenIssuer;

    public String findRefreshTokenByUserEmail(String email) {

        return redisTemplate.opsForValue().get(email);
    }

    public boolean refreshTokenAuthentication(String refreshToken, String storedRefreshToken) {
        return Objects.equals(refreshToken, storedRefreshToken);
    }

    public TokenDto reissueAccessToken(String email) {

        TokenDto tokenIssueResult = new TokenDto();

        tokenIssueResult.setAccessToken(accessTokenIssuer.accessTokenIssue(email));
        tokenIssueResult.setMessage("Access Token이 정상 발급 되었습니다.");

        return tokenIssueResult;
    }
}
