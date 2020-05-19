package net.thumbtack.school.hospital.validation;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxNameLengthValidator implements ConstraintValidator<MaxNameLength, String> {

    @Value("${max_name_length}")
    private int maxNameLength;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext cvx) {
        return s.length() <= maxNameLength;
    }

}
