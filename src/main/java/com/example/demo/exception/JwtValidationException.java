package com.example.demo.exception;

public class JwtValidationException extends RuntimeException  {
    public JwtValidationException(String message) {
        super(message);
    }
}
