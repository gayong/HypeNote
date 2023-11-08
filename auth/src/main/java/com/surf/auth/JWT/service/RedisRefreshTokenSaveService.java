package com.surf.auth.JWT.service;

import com.surf.auth.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisRefreshTokenSaveService {
    private final StringRedisTemplate redisTemplate;

    public void saveRefreshToken(long EXPIRATION_TIME, String refreshToken, User userInfo) {

        int refreshTokenKey = userInfo.getUserPk();

        redisTemplate.opsForValue().set(String.valueOf(refreshTokenKey), refreshToken, EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }
}
