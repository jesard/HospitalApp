package net.thumbtack.school.hospital.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MobilePhoneValidator implements ConstraintValidator<MobilePhone, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx) {
        if (s == null) return false;
        if (s.replace("-", "").replace("+7", "8").length() != 11
                || !s.matches("^(\\+7|8)9[0-9\\-]+$")) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(
                    "Invalid mobile phone %s").addConstraintViolation();
            return false;
        }
        return true;
    }
}
