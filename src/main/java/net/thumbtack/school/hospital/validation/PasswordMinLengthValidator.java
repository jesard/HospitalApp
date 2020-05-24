package net.thumbtack.school.hospital.validation;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMinLengthValidator implements ConstraintValidator<PasswordMinLength, String> {

    @Value("${min_password_length}")
    private int minPasswordLength;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx) {
        if (s == null) return false;
        if (s.length() < minPasswordLength) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(
                    "Too short password").addConstraintViolation();
            return false;
        }
        return true;
    }
}
