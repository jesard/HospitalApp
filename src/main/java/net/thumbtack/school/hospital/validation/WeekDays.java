package net.thumbtack.school.hospital.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = WeekDaysValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface WeekDays {
    String message() default "Invalid weekdays";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
