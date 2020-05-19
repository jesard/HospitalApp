package net.thumbtack.school.hospital.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = WeekDayValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface WeekDay {
    String message() default "Invalid weekday";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
