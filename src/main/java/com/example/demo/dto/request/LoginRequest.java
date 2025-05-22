package com.example.demo.dto.request;

import com.example.demo.validation.ValidPassword;
import com.example.demo.validation.ValidUsername;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @ValidUsername
    @Schema(description = "아이디", example = "test")
    private String username;
    @ValidPassword
    @Schema(description = "비밀번호", example = "test1234")
    private String password;
}
