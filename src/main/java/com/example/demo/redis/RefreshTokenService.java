package com.example.demo.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final long refreshTokenExpireTime = 7 * 24 * 60 * 60 * 1000L; // 7일

    public void saveRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue().set("RT:" + username, refreshToken, refreshTokenExpireTime, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get("RT:" + username);
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete("RT:" + username);
    }
}
