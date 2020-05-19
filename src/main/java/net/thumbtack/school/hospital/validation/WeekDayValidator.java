package net.thumbtack.school.hospital.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class WeekDayValidator implements ConstraintValidator<WeekDay, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        List<String> weekDays = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri");
        return weekDays.contains(s);
    }
}
