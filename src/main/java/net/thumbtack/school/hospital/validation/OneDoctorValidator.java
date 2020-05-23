package net.thumbtack.school.hospital.validation;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OneDoctorValidator implements ConstraintValidator<OneDoctor, Object> {



    @Override
    public boolean isValid(Object o, ConstraintValidatorContext ctx) {

        Integer doctorId = (Integer) new BeanWrapperImpl(o).getPropertyValue("doctorId");
        String speciality = (String) new BeanWrapperImpl(o).getPropertyValue("speciality");

        if (doctorId == null || doctorId == 0) {
            if (speciality != null && speciality.length() != 0) {
                return true;
            }
        }
        if (speciality == null || speciality.length() == 0) return true;

        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(
                "One of (doctorId, speciality) should be empty").addConstraintViolation();
        return false;


    }
}
