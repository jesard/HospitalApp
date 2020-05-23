package net.thumbtack.school.hospital.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeValidator implements ConstraintValidator<Time, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx) {
        if (s == null) return true;
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalTime.parse(s, formatterTime);
        } catch (DateTimeParseException ex) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(
                    "Invalid time %s").addConstraintViolation();
            return false;
        }
        return true;
    }
}
