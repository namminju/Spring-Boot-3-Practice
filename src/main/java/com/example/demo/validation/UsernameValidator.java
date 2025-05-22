package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(username)) {
            setMessage(context, "아이디는 필수 입력 항목입니다.");
            return false;
        }

        if (username.length() < 4 || username.length() > 20) {
            setMessage(context, "아이디는 4자 이상 20자 이하로 입력해주세요.");
            return false;
        }

        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
