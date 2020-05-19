package net.thumbtack.school.hospital.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeValidator implements ConstraintValidator<Time, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");;
            LocalDate.parse(s, formatterTime);
        } catch (DateTimeParseException ex) {
            return false;
        }
        return true;
    }
}
