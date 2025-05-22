package com.example.demo.dto.request;

import com.example.demo.validation.ValidPassword;
import com.example.demo.validation.ValidUsername;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    @Schema(description = "이름", example = "홍길동")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @Schema(description = "별명", example = "행복한 잠자리")
    private String nickname;

    @ValidUsername
    @Schema(description = "아이디", example = "test")
    private String username;

    @ValidPassword
    @Schema(description = "비밀번호", example = "test1234")
    private String password;
}
