package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.Doctor;

public interface DoctorDao extends UserDao {

    int getSpecialityId(String speciality);

    int getRoomId(String speciality);

    Doctor insertDoctor(Doctor doctor);

    Doctor insertDoctorWithSchedule(Doctor doctor);

    Doctor getDoctorByLogin(String login);
}
