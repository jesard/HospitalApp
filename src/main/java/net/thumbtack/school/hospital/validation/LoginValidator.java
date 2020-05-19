package net.thumbtack.school.hospital.validation;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<Login, String> {

        @Value("${max_name_length}")
        private int maxNameLength;

        @Override
        public boolean isValid(String s, ConstraintValidatorContext cvx) {
            return s != null
                    && s.length() > 0
                    && s.length() < maxNameLength
                    && s.matches("^[а-яА-ЯёЁa-zA-Z0-9]+$");
        }

}
