package net.thumbtack.school.hospital.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OneScheduleValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface OneSchedule {
    String message() default "One schedule should be empty";
    String[] fields();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
