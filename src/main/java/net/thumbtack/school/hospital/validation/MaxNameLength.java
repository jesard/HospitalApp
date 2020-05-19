package net.thumbtack.school.hospital.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxNameLengthValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)

public @interface MaxNameLength {
    String message() default "Max name length exceeded";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
