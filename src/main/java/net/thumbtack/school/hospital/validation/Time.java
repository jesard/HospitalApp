package net.thumbtack.school.hospital.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TimeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Time {
    String message() default "Invalid time format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
