package net.thumbtack.school.hospital.validation;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxNameLengthValidator implements ConstraintValidator<MaxNameLength, String> {

    @Value("${max_name_length}")
    private int maxNameLength;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx) {

        if (s.length() > maxNameLength) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(
                    "Too long %s").addConstraintViolation();
            return false;
        }
        return true;
    }

}
