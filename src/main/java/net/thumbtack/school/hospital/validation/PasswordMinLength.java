package net.thumbtack.school.hospital.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMinLengthValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMinLength {
    String message() default "Too short password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
