package com.example.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
public @interface ValidUsername {
    String message() default "아이디 형식이 잘못되었습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
