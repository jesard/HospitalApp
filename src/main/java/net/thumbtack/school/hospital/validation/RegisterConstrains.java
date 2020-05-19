package net.thumbtack.school.hospital.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RegisterConstrainsValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface RegisterConstrains {
    String message() default "Invalid registration parameters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
