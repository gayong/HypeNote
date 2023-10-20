package com.example.securitystudy.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthDao {
    private JdbcTemplate jdbcTemplate;
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public AuthDao(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Long insertRefreshToken(Long userid, String refreshToken) {
        redisTemplate.opsForValue().set(userid.toString(), refreshToken);
        return userid;
    }

    public String updateRefreshToken(Long userid, String newRefreshToken) {
        redisTemplate.opsForValue().set(userid.toString(), newRefreshToken);
        return newRefreshToken;
    }

//    public boolean checkRefreshToken(String token) {
//        String checkRefreshTokenQuery = "select exists(select refresh_token from token where refresh_token = ?)";
//
//        int result = this.jdbcTemplate.queryForObject(checkRefreshTokenQuery,int.class,token);
//
//        if (result != 1)
//            return false;
//
//        return true;
//    }
    public boolean checkRefreshToken(String token) {
        return redisTemplate.opsForValue().get(token) != null;
    }

//    public boolean checkUser(Long userid) {
//        String checkUserQuery = "select exists(select user_id from token where user_id=?)";
//
//        int result = this.jdbcTemplate.queryForObject(checkUserQuery, int.class, userid);
//
//        if (result != 1)
//            return false;
//
//        return true;
//    }
    public boolean checkUser(Long userid) {
        return redisTemplate.hasKey(userid.toString());
    }
}
