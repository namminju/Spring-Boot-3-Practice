package com.example.demo.controller;

import com.example.demo.dto.error.ErrorResponse;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.ReissueRequest;
import com.example.demo.dto.request.SignupRequest;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.dto.success.SuccessResponse;
import com.example.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// AuthController.java
@Tag(name = "\uD83D\uDC64 Auth API", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "이름, 닉네임, 아이디(username), 비밀번호")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "정상 처리",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = SuccessResponse.class))),
//            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "500", description = "서버 오류",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ErrorResponse.class)))
//    })
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse> signup(
            @RequestBody @Validated SignupRequest request,
            HttpServletRequest httpRequest
    ) {
        authService.signup(request);

        SuccessResponse response = SuccessResponse.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .status(HttpStatus.OK.value())
                .message("회원가입이 완료되었습니다.")
                .path(httpRequest.getRequestURI())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "로그인",
            description = "아이디(username), 비밀번호"
    )
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        SuccessResponse response = SuccessResponse.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .status(HttpStatus.OK.value())
                .message("로그인을 성공하였습니다.")
                .path(httpRequest.getRequestURI())
                .data(authService.login(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그아웃", description = "AccessToken을 만료 처리",  security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse> logout(HttpServletRequest httpRequest) {
        String authorization = httpRequest.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    SuccessResponse.builder()
                            .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("유효한 Authorization 헤더가 없습니다.")
                            .path(httpRequest.getRequestURI())
                            .build()
            );
        }

        String accessToken = authorization.substring(7); // "Bearer " 제외

        authService.logout(accessToken);

        SuccessResponse response = SuccessResponse.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .status(HttpStatus.OK.value())
                .message("로그아웃이 완료되었습니다.")
                .path(httpRequest.getRequestURI())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "AccessToken 재발급", description = "RefreshToken을 이용하여 AccessToken을 재발급합니다.")
    @PostMapping("/reissue")
    public ResponseEntity<SuccessResponse> reissue(
            @RequestBody ReissueRequest request,
            HttpServletRequest httpRequest) {

        TokenResponse tokenResponse = authService.reissue(request);

        SuccessResponse response = SuccessResponse.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .status(HttpStatus.OK.value())
                .message("AccessToken이 재발급되었습니다.")
                .path(httpRequest.getRequestURI())
                .data(tokenResponse)
                .build();

        return ResponseEntity.ok(response);
    }

}
