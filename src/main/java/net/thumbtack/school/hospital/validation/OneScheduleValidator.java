package net.thumbtack.school.hospital.validation;

import net.thumbtack.school.hospital.dto.request.regdoctor.WeekDaysSchedule;
import net.thumbtack.school.hospital.dto.request.regdoctor.WeekSchedule;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class OneScheduleValidator implements ConstraintValidator<OneSchedule, Object> {

    private String[] oneNotNull;

    public void initialize(OneSchedule constraintAnnotation) {
        oneNotNull = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext ctx) {
        boolean isValid;
        String field1 = oneNotNull[0];
        String field2 = oneNotNull[1];
        WeekSchedule obj1 = (WeekSchedule) new BeanWrapperImpl(obj).getPropertyValue(field1);
        List<WeekDaysSchedule> obj2 = (List<WeekDaysSchedule>) new BeanWrapperImpl(obj).getPropertyValue(field2);
        isValid = (obj1 == null || obj1.getTimeStart() == null) != (obj2 == null || obj2.size() == 0);
        if (!isValid) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(
                    "One schedule should be empty: {" + field1 + ", " + field2 + "}")
                    .addConstraintViolation();
        }
        return isValid;
    }
}
