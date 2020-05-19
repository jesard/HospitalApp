package net.thumbtack.school.hospital.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MobilePhoneValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MobilePhone {
    String message() default "Invalid mobile phone number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
