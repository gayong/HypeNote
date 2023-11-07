package com.surf.auth.JWT.service;

import com.surf.auth.auth.dto.TokenDto;
import com.surf.auth.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ReissueService {

    private final StringRedisTemplate redisTemplate;
    private final AccessTokenIssueService accessTokenIssueService;

    @Value("${jwt.secret}")
    private String SECRET;
    public User parsingRefreshToken(String refreshToken) {

        User userInfo = new User();

        userInfo.setUserId();
        userInfo.setEmail();
        userInfo.setNickName();
        userInfo.setProfileImage();

        return userInfo;
    }

    public String findRefreshTokenByUserId(String userId) {

        return redisTemplate.opsForValue().get(userId);
    }

    public boolean refreshTokenAuthentication(String refreshToken, String storedRefreshToken) {
        return Objects.equals(refreshToken, storedRefreshToken);
    }

    public TokenDto reissueAccessToken(User userInfo) {

        TokenDto tokenIssueResult = new TokenDto();

        tokenIssueResult.setAccessToken(accessTokenIssueService.accessTokenIssue(userInfo));
        tokenIssueResult.setMessage("Access Token이 정상 발급 되었습니다.");

        return tokenIssueResult;
    }
}
