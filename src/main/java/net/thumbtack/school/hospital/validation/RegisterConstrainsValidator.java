package net.thumbtack.school.hospital.validation;

import net.thumbtack.school.hospital.dto.request.RegUserDtoRequest;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RegisterConstrainsValidator implements ConstraintValidator<RegisterConstrains, RegUserDtoRequest> {

    @Value("${max_name_length}")
    private int maxNameLength;

    @Override
    public void initialize(RegisterConstrains constraintAnnotation) {

    }

    @Override
    public boolean isValid(RegUserDtoRequest request, ConstraintValidatorContext ctx) {
        String firstName = request.getFirstName();
        if (firstName == null
                || firstName.length() == 0
                || firstName.length() > maxNameLength
                || !firstName.matches("^[а-яА-ЯёЁ\\-\\s]+$")) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(
                    "Wrong first name")
                    .addPropertyNode("firstName").addConstraintViolation();
            return false;
        }
        return true;
    }
}
