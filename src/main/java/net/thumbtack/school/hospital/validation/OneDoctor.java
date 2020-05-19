package net.thumbtack.school.hospital.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OneDoctorValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface OneDoctor {
    String message() default "Doctor id or speciality, not both";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
