package net.thumbtack.school.hospital.validation;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMinLengthValidator implements ConstraintValidator<PasswordMinLength, String> {

    @Value("${min_password_length}")
    private int minPasswordLength;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.length() >= minPasswordLength;
    }
}
