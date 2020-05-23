package net.thumbtack.school.hospital.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class WeekDaysValidator implements ConstraintValidator<WeekDays, List<String>> {
    @Override
    public boolean isValid(List<String> strings, ConstraintValidatorContext ctx) {
        List<String> weekDays = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri");
        for(String dayOfWeek: strings) {
            if (!weekDays.contains(dayOfWeek)) {
                ctx.disableDefaultConstraintViolation();
                ctx.buildConstraintViolationWithTemplate(
                        "Invalid day of week %s").addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
