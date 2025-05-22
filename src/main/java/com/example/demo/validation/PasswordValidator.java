package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(value)) {
            setMessage(context, "비밀번호는 필수 입력 항목입니다.");
            return false;
        }

        if (value.length() < 8 || value.length() > 20) {
            setMessage(context, "비밀번호는 8자 이상 20자 이하로 입력해주세요.");
            return false;
        }

        if (!value.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*]{8,20}$")) {
            setMessage(context, "비밀번호는 영문과 숫자를 포함하여 8~20자여야 합니다.");
            return false;
        }

        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
