package net.thumbtack.school.hospital.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RussianNameValidator implements ConstraintValidator<RussianName, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.matches("^[а-яА-ЯёЁ\\-\\s]+$");
    }
}
