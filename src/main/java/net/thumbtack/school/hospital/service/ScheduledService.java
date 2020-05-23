package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.DoctorDao;
import net.thumbtack.school.hospital.daoimpl.DoctorDaoImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ScheduledService {

    private DoctorDao doctorDao = new DoctorDaoImpl();

    @Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
    public int deleteDoctorsWithTerminationDate() {
        return doctorDao.deleteDoctorsWithTerminationDate(LocalDate.now());
    }
}
