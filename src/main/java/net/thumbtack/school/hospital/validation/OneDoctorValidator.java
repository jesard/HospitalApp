package net.thumbtack.school.hospital.validation;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OneDoctorValidator implements ConstraintValidator<OneDoctor, Object> {



    @Override
    public boolean isValid(Object o, ConstraintValidatorContext ctx) {

        Integer doctorId = (Integer) new BeanWrapperImpl(o).getPropertyValue("doctorId");
        String speciality = (String) new BeanWrapperImpl(o).getPropertyValue("speciality");

        return (doctorId == null || doctorId == 0) == (speciality == null || speciality.length() == 0);
    }
}
