package com.example.demo.service;

import com.example.demo.domain.Account;
import com.example.demo.domain.Member;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.ReissueRequest;
import com.example.demo.dto.request.SignupRequest;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.exception.JwtValidationException;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    /**
     * 회원가입
     */
    public void signup(SignupRequest request) {
        if (memberRepository.findByAccountUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Account account = new Account(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        Member member = new Member(null, request.getName(), request.getNickname(), account);
        memberRepository.save(member);
    }

    /**
     * 로그인: 인증 + 토큰 발급 + Redis 저장
     */
    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByAccountUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getAccount().getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        String username = member.getAccount().getUsername();
        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);

        // Refresh Token을 Redis에 저장
        tokenService.saveRefreshToken(username, refreshToken, jwtUtil.getRefreshTokenDuration());

        return new TokenResponse(accessToken, refreshToken);
    }

    public void logout(String accessToken) {
        String username = jwtUtil.extractUsername(accessToken);
        tokenService.deleteRefreshToken(username);
        long remainingMs = jwtUtil.getRemainingExpiration(accessToken);
        tokenService.blacklistAccessToken(accessToken, remainingMs);
    }

    public TokenResponse reissue(ReissueRequest request) {
        String username = request.getUsername();
        String refreshToken = request.getRefreshToken();

        String stored = tokenService.getRefreshToken(username);
        if (stored == null || !stored.equals(refreshToken)) {
            throw new JwtValidationException("올바른 refresh token이 아닙니다.");
        }

        String newAccessToken = jwtUtil.generateAccessToken(username);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        tokenService.saveRefreshToken(username, newRefreshToken, jwtUtil.getRefreshTokenDuration());

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

}