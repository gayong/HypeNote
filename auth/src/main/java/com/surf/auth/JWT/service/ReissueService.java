package com.surf.auth.JWT.service;

import com.surf.auth.JWT.provider.SignKeyProvider;
import com.surf.auth.auth.dto.TokenDto;
import com.surf.auth.auth.dto.UserDto;
import com.surf.auth.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

import javax.crypto.SecretKey;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ReissueService {

    private final StringRedisTemplate redisTemplate;
    private final AccessTokenIssueService accessTokenIssueService;

    public String findRefreshTokenByUserPk(String userPk) {

        return redisTemplate.opsForValue().get(userPk);
    }

    public boolean refreshTokenAuthentication(String refreshToken, String storedRefreshToken) {
        return Objects.equals(refreshToken, storedRefreshToken);
    }

    public TokenDto reissueAccessToken(UserDto userInfo) {

        TokenDto tokenIssueResult = new TokenDto();

        tokenIssueResult.setAccessToken(accessTokenIssueService.accessTokenIssue(userInfo));
        tokenIssueResult.setMessage("Access Token이 정상 발급 되었습니다.");

        return tokenIssueResult;
    }
}
