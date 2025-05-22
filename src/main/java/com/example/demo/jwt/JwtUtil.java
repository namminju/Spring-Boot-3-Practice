package com.example.demo.jwt;

import com.example.demo.exception.JwtValidationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    private SecretKey secretKey;
    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    @PostConstruct
    public void init() {
        if (secretKeyString.getBytes(StandardCharsets.UTF_8).length < 64) {
            throw new IllegalArgumentException("Secret key must be at least 64 bytes for HS512");
        }
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    }

    /** Access Token 생성 */
    public String generateAccessToken(String username) {
        return createToken(username, accessTokenExpirationMs);
    }

    /** Refresh Token 생성 */
    public String generateRefreshToken(String username) {
        return createToken(username, refreshTokenExpirationMs);
    }

    private String createToken(String username, long expirationMs) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey, SIGNATURE_ALGORITHM)
                .compact();
    }

    /** 토큰에서 사용자명 추출 */
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /** 남은 만료 시간 계산 */
    public long getRemainingExpiration(String token) {
        Date expiration = parseClaims(token).getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }

    /** 토큰 유효성 확인 - 예외 throw */
    public void validateTokenOrThrow(String token) {
        try {
            parseClaims(token);
        } catch (ExpiredJwtException e) {
            throw new JwtValidationException("토큰이 만료되었습니다.");
        } catch (SignatureException e) {
            throw new JwtValidationException("서명이 유효하지 않습니다.");
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new JwtValidationException("잘못된 형식의 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtValidationException("토큰이 비어있거나 유효하지 않습니다.");
        }
    }

    /** 토큰 유효성 확인 - boolean 반환 */
    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /** 공통 Claims 파싱 로직 */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /** Refresh Token 유효 시간 반환 */
    public long getRefreshTokenDuration() {
        return refreshTokenExpirationMs;
    }
}
