package com.example.demo.exception;

import com.example.demo.dto.error.ErrorResponse;
import com.example.demo.dto.error.FieldErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Hidden
@RestControllerAdvice // @ControllerAdvice + @ResponseBody
public class GlobalExceptionHandler {

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        // 필드명 → 여러 개의 오류 메시지 리스트
        Map<String, List<String>> fieldErrorMap = new LinkedHashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String field = error.getField();
            fieldErrorMap.computeIfAbsent(field, key -> new ArrayList<>()).add(error.getDefaultMessage());
        }

        // 모든 오류 메시지를 각각 하나의 FieldErrorResponse로 변환
        List<FieldErrorResponse> fieldErrors = fieldErrorMap.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(msg -> FieldErrorResponse.builder()
                                .field(entry.getKey())
                                .message(msg)
                                .build()))
                .collect(Collectors.toList());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(getCurrentTimestamp())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .code("VALIDATION-400")
                .message("입력값 오류가 발생했습니다.")
                .errorDetails(fieldErrors)
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(getCurrentTimestamp())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .code("COMMON-400")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(
            CustomException ex, HttpServletRequest request) {

        ErrorCode code = ex.getErrorCode();

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(getCurrentTimestamp())
                .status(code.getStatus().value())
                .error(code.getStatus().getReasonPhrase())
                .code(code.getCode())
                .message(code.getMessage())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return ResponseEntity.status(code.getStatus()).body(response);
    }

    //토큰 오류
    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<String> handleJwtValidation(JwtValidationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(getCurrentTimestamp())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .code("COMMON-500")
                .message("서버 내부 오류가 발생했습니다.")
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


}
