package net.thumbtack.school.hospital.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RussianNameValidator implements ConstraintValidator<RussianName, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx) {
        if (s == null) return false;
        if (!s.matches("^[а-яА-ЯёЁ\\-\\s]+$")) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(
                    "Invalid russian name %s").addConstraintViolation();
            return false;
        }
        return true;
    }
}
