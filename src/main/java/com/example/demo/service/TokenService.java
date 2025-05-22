package com.example.demo.service;

import com.example.demo.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    // RefreshToken 저장 (key: username, value: refreshToken)
    public void saveRefreshToken(String username, String refreshToken, long durationMs) {
        redisTemplate.opsForValue().set("RT:" + username, refreshToken, durationMs, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get("RT:" + username);
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete("RT:" + username);
    }

    // AccessToken 블랙리스트 등록 (key: token, value: "logout")
    public void blacklistAccessToken(String token, long expiration) {
        redisTemplate.opsForValue().set("BL:" + token, "logout", expiration, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey("BL:" + token);
    }
}
