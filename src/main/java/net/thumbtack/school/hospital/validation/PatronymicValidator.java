package net.thumbtack.school.hospital.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PatronymicValidator implements ConstraintValidator<Patronymic, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx) {
        if (s == null) return true;
        if (!s.matches("^[а-яА-ЯёЁ\\-\\s]*$")) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(
                    "Invalid russian name %s").addConstraintViolation();
            return false;
        }
        return true;
    }
}
