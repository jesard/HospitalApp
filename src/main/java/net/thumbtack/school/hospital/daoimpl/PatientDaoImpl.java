package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.PatientDao;
import net.thumbtack.school.hospital.error.Field;
import net.thumbtack.school.hospital.error.MyError;
import net.thumbtack.school.hospital.error.ServerErrorCode;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
    public int getUserIdByPatientId(int patientId) {
        LOGGER.debug("DAO get UserId By PatientId: {}", patientId);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPatientMapper(sqlSession).getUserIdByPatientId(patientId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get UserId By PatientId {} - {}", patientId, ex);
                throw ex;
            }
        }
    }

    @Override
    public Patient getPatientByUserId(int userId) {
        LOGGER.debug("DAO get Patient with id: {}", userId);
        User user = getUserById(userId);
        try (SqlSession sqlSession = getSession()) {
            try {
                Patient patient = getPatientMapper(sqlSession).getPatientByUserId(userId);
                patient.setUserId(userId);
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
    public void updatePatient(Patient newPatient) {
        LOGGER.debug("DAO update Patient {}", newPatient.getLastName());
        updateUser(newPatient);
        try (SqlSession sqlSession = getSession()) {
            try {
                getPatientMapper(sqlSession).update(newPatient);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't update Patient {} - {}", newPatient.getLastName(), ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

//    @Override
//    public void makeAppointment(int patientId, List<Integer> slotIds, String ticketNumber) throws ServerException {
//        LOGGER.debug("DAO make Appointment for Patient {} for few slots", patientId);
//        int updatedRows;
//        try (SqlSession sqlSession = getSession()) {
//            try {
//                for (int slotId: slotIds) {
//                    updatedRows = getPatientMapper(sqlSession).makeAppointment(patientId, slotId, ticketNumber);
//                    if (updatedRows == 0) {
//                        throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
//                    }
//                }
//            } catch (RuntimeException | ServerException ex) {
//                LOGGER.info("Can't make Appointment for Patient {} for few slots - {}", patientId, ex);
//                sqlSession.rollback();
//                throw ex;
//            }
//            sqlSession.commit();
//        }
//    }

    @Override
    public int makeAppointment(int patientId, int slotId, String ticketNumber) throws ServerException {
        LOGGER.debug("DAO make Appointment for Patient {}", patientId);
        int updatedRows;
        try (SqlSession sqlSession = getSession()) {
            try {
                updatedRows = getPatientMapper(sqlSession).makeAppointment(patientId, slotId, ticketNumber);
                if (updatedRows == 0) {
                    throw new ServerException(new MyError(ServerErrorCode.SLOT_IS_BUSY, Field.DATETIME));
                }
            } catch (RuntimeException | ServerException ex) {
                LOGGER.info("Can't make Appointment for Patient {} - {}", patientId, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return updatedRows;
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

    @Override
    public void deleteTicket(String ticketNumber) {
        LOGGER.debug("DAO delete ticket with number {}", ticketNumber);
        try (SqlSession sqlSession = getSession()) {
            try {
                getPatientMapper(sqlSession).deleteTicket(ticketNumber);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete ticket with number {} - {}", ticketNumber, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public int getPatientIdByTicketNumber(String ticketNumber) {
        LOGGER.debug("DAO get Patient id by ticket number {}", ticketNumber);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPatientMapper(sqlSession).getPatientIdByTicketNumber(ticketNumber);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get Patient id by ticket number {} - {}", ticketNumber, ex);
                throw ex;
            }
        }
    }

    @Override
    public int getPatientAppointmentsNumber(int patientId, String startDate, String endDate) {
        LOGGER.debug("DAO get Patient with id {} appointments number from {} to {}", patientId, startDate, endDate);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPatientMapper(sqlSession).getPatientAppointmentsNumber(patientId, startDate, endDate);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get Patient with id {} appointments number from {} to {} - {}", patientId, startDate, endDate, ex);
                throw ex;
            }
        }
    }
}
