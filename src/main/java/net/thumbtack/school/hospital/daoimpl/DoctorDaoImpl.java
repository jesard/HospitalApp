package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.DoctorDao;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Slot;
import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoctorDaoImpl extends UserDaoImpl implements DoctorDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDaoImpl.class);

    @Override
    public int getSpecialityId(String speciality) {
        LOGGER.debug("DAO get speciality id {}", speciality);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getDoctorMapper(sqlSession).getSpecialityId(speciality);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get speciality {}: {}", speciality, ex);
                throw ex;
            }
        }
    }

    @Override
    public int getRoomId(String room) {
        LOGGER.debug("DAO get room id {}", room);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getDoctorMapper(sqlSession).getRoomId(room);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get room {}: {}", room, ex);
                throw ex;
            }
        }
    }

    @Override
    public Doctor insertDoctor(Doctor doctor) {
        LOGGER.debug("DAO insert Doctor {}", doctor);
        int specialityId = getSpecialityId(doctor.getSpeciality());
        int roomId = getRoomId(doctor.getRoom());
        insertUser(doctor, "doctor");
        try (SqlSession sqlSession = getSession()) {
            try {
                getDoctorMapper(sqlSession).insert(doctor, specialityId, roomId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Doctor {}: {}", doctor, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return doctor;
    }

    @Override
    public Doctor insertDoctorWithSchedule(Doctor doctor) {
        LOGGER.debug("DAO insert Doctor with Schedule {}", doctor.getLastName());
        try (SqlSession sqlSession = getSession()) {
            try {
                int specialityId = getSpecialityId(doctor.getSpeciality());
                int roomId = getRoomId(doctor.getRoom());
                insertUser(doctor, "doctor");
                getDoctorMapper(sqlSession).insert(doctor, specialityId, roomId);
                for (DaySchedule daySchedule: doctor.getSchedule()) {
                    getScheduleMapper(sqlSession).insertDaySchedule(daySchedule, doctor.getId());
                    for (Slot slot: daySchedule.getSlotSchedule()) {
                        getScheduleMapper(sqlSession).insertSlot(slot, daySchedule.getId());
                    }
                }

            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Doctor with Schedule {}: {}", doctor.getLastName(), ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return doctor;
    }


    @Override
    public Doctor getDoctorByLogin(String login) {
        LOGGER.debug("DAO get Doctor with login: {}", login);
        User user = getUser(login);
        try (SqlSession sqlSession = getSession()) {
            try {
                Doctor doctor = getDoctorMapper(sqlSession).getDoctorByUserId(user.getUserId());
                doctor.setFirstName(user.getFirstName());
                doctor.setLastName(user.getLastName());
                doctor.setLogin(user.getLogin());
                doctor.setPassword(user.getPassword());
                return doctor;
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get Doctor with login {}: {}", login, ex);
                throw ex;
            }
        }
    }
}
