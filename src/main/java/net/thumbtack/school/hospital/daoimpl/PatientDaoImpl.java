package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.PatientDao;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;

public class PatientDaoImpl extends UserDaoImpl implements PatientDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientDaoImpl.class);

    @Override
    public Patient insertPatient(Patient patient) {
        LOGGER.debug("DAO insert Patient {}", patient);
        insertUser(patient, "patient");
        try (SqlSession sqlSession = getSession()) {
            try {
                getPatientMapper(sqlSession).insert(patient);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Patient {}: {}", patient, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return patient;
    }

    @Override
    public Patient getPatientByUserId(int userId) {
        LOGGER.debug("DAO get Patient with id: {}", userId);
        User user = getUserById(userId);
        try (SqlSession sqlSession = getSession()) {
            try {
                Patient patient = getPatientMapper(sqlSession).getPatientByUserId(userId);
                patient.setFirstName(user.getFirstName());
                patient.setLastName(user.getLastName());
                patient.setPatronymic(user.getPatronymic());
                patient.setLogin(user.getLogin());
                patient.setPassword(user.getPassword());
                return patient;
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get Patient with id {}: {}", userId, ex);
                throw ex;
            }
        }
    }

    @Override
    public void makeAppointment(Patient patient, int slotId, String ticketNumber) {
        LOGGER.debug("DAO make Appointment for Patient {}", patient.getLastName());
        try (SqlSession sqlSession = getSession()) {
            try {
                getPatientMapper(sqlSession).makeAppointment(patient, slotId, ticketNumber);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't make Appointment for Patient {} - {}", patient.getLastName(), ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public int getSlotIdByDateTime(int doctorId, LocalDate date, LocalTime timeStart) {
        LOGGER.debug("DAO get SlotId by doctorId {}, date {}, time {}", doctorId, date, timeStart);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getScheduleMapper(sqlSession).getSlotIdByDateTime(doctorId, date, timeStart);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get SlotId by doctorId {}, date {}, time {} - {}", doctorId, date, timeStart, ex);
                throw ex;
            }
        }
    }

}
