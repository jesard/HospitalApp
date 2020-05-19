package net.thumbtack.school.hospital.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RussianNameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RussianName {
    String message() default "Invalid russian name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
