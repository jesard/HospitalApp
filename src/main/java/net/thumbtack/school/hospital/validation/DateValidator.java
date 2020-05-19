package net.thumbtack.school.hospital.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator implements ConstraintValidator<Date, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate.parse(s, formatterDate);
        } catch (DateTimeParseException ex) {
            return false;
        }
        return true;
    }
}
