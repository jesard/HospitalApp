package net.thumbtack.school.hospital.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MobilePhoneValidator implements ConstraintValidator<MobilePhone, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.length() >= 11
                && s.replace("-","").length() <= 12
                && s.matches("^(\\+7|8)9[0-9\\-]+$");
    }
}
