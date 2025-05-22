package com.example.demo.dto.success;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse {

    private String timestamp;
    private int status;
    private String message;
    private String path;
    private Object data; // ✅ 유연한 타입 대응 (List, 단일 DTO 등)
}
