package com.example.demo.filter;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            // ✅ 블랙리스트 확인
            if (tokenService.isBlacklisted(token)) {
                // 로그아웃된 토큰 → 인증 실패 처리
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "로그아웃된 토큰입니다.");
                return;
            }

            try {
                // 검증 실패 시 예외 발생
                jwtUtil.validateTokenOrThrow(token);

                // 검증 성공 후 사용자 정보 추출
                String username = jwtUtil.extractUsername(token);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, List.of()); // 권한 부여 로직 필요 시 추가

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException e) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "토큰이 만료되었습니다.");
                return;
            } catch (SignatureException e) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "서명이 유효하지 않습니다.");
                return;
            } catch (Exception e) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    //따로 파일 생성 필요. 임시 조치
    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
