package com.example.demo.dto.error;

import com.example.demo.dto.error.FieldErrorResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    // 언제?
    private String timestamp;

    // 어떤 요청?
    private String method;
    private String path;

    // 어떤 에러? - 상태 코드 및 코드 설명
    private int status; //상태 코드
    private String error;  // HTTP 상태 설명, 예: "Bad Request"
    private String code;   // 비즈니스 오류 코드, 예: "USER-404"

    // 왜 에러? - 사용자/개발자 메시지
    private String message;

    // 어떤 필드? - 필드별 상세 오류 (유효성 검사 등)
    private List<FieldErrorResponse> errorDetails;
}
