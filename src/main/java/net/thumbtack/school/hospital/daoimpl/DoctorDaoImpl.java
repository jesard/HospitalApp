package net.thumbtack.school.hospital.daoimpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import net.thumbtack.school.hospital.dao.DoctorDao;
import net.thumbtack.school.hospital.error.Field;
import net.thumbtack.school.hospital.error.MyError;
import net.thumbtack.school.hospital.error.ServerErrorCode;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.*;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DoctorDaoImpl extends UserDaoImpl implements DoctorDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDaoImpl.class);

    private void deleteSchedule(int doctorId, LocalDate startDate, LocalDate endDate, SqlSession sqlSession) {
        LOGGER.debug("DAO delete schedule for doctor with id {}", doctorId);
        try {
            for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
                getScheduleMapper(sqlSession).deleteSchedule(doctorId, date);
            }
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete schedule for doctor with id {} - {}", doctorId , ex);
            sqlSession.rollback();
            throw ex;
        }

    }

    private void deleteRoomOccupation(int doctorId, LocalDate startDate, LocalDate endDate, SqlSession sqlSession) {
        LOGGER.debug("DAO delete occupation for doctor with id {}", doctorId);
        try {
            for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
                getDoctorMapper(sqlSession).deleteRoomOccupation(doctorId, date);
            }
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete occupation for doctor with id {} - {}", doctorId, ex);
            sqlSession.rollback();
            throw ex;
        }
    }

    private void deleteRoomOccupationFromDate(int doctorId, LocalDate startDate, SqlSession sqlSession) {
        LOGGER.debug("DAO delete occupation for doctor with id {}", doctorId);
        try {
            getDoctorMapper(sqlSession).deleteRoomOccupationFromDate(doctorId, startDate);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete occupation for doctor with id {} - {}", doctorId, ex);
            sqlSession.rollback();
            throw ex;
        }
    }

    private boolean shouldReplaceSchedule(List<DaySchedule> oldSchedule, LocalDate newStartDate, LocalDate newEndDate) {
        for (DaySchedule day: oldSchedule) {
            if (day.getDate().isAfter(newStartDate.minusDays(1)) && day.getDate().isBefore(newEndDate.plusDays(1))) {
                for (Slot slot: day.getSlotSchedule()) {
                    if (slot.getPatient() != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public int getSpecialityId(String speciality) throws ServerException {
        LOGGER.debug("DAO get speciality id {}", speciality);
        //TODO double-try required?
        try (SqlSession sqlSession = getSession()) {
            try {
                return getDoctorMapper(sqlSession).getSpecialityId(speciality);
            } catch (BindingException ex) {
                LOGGER.info("Speciality not found {}", speciality, ex);
                throw new ServerException(new MyError(ServerErrorCode.SPECIALITY_NOT_FOUND, Field.SPECIALITY));
            } catch (PersistenceException ex) {
                LOGGER.info("Can't get speciality {}: {}", speciality, ex);
                throw ex;
            }
        }
    }

    @Override
    public int getRoomId(String room) throws ServerException {
        LOGGER.debug("DAO get room id {}", room);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getDoctorMapper(sqlSession).getRoomId(room);
            } catch (BindingException ex) {
                LOGGER.info("ROOM_NOT_FOUND {}: {}", room, ex);
                throw new ServerException(new MyError(ServerErrorCode.ROOM_NOT_FOUND, Field.ROOM));
            } catch (PersistenceException ex) {
                LOGGER.info("Can't get room {}: {}", room, ex);
                throw ex;
            }
        }
    }

    @Override
    public int getRoomId(int doctorId) throws ServerException {
        LOGGER.debug("DAO get room for doctor with id {}", doctorId);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getDoctorMapper(sqlSession).getRoomIdByDoctorId(doctorId);
            } catch (BindingException ex) {
                LOGGER.info("ROOM_NOT_FOUND", ex);
                throw new ServerException(new MyError(ServerErrorCode.ROOM_NOT_FOUND, Field.ROOM));
            } catch (PersistenceException ex) {
                LOGGER.info("Can't get room", ex);
                throw ex;
            }
        }
    }

    @Override
    public Doctor insertDoctor(Doctor doctor) throws ServerException {
        LOGGER.debug("DAO insert Doctor {}", doctor);
        int specialityId = getSpecialityId(doctor.getSpeciality());
        int roomId = getRoomId(doctor.getRoom());
        List<DaySchedule> schedule = doctor.getSchedule();
        boolean roomIsFree = true;
        try (SqlSession sqlSession = getSession()) {
            try {
                for (DaySchedule day: schedule) {
                    List<Slot> slotSchedule = day.getSlotSchedule();
                    LocalTime timeStart = slotSchedule.get(0).getTimeStart();
                    LocalTime timeEnd = slotSchedule.get(slotSchedule.size() - 1).getTimeEnd();
                    int count = getDoctorMapper(sqlSession).getDoctorIdCountByRoomDateTime(day.getDate(), timeStart, timeEnd, roomId);
                    if (count != 0 ) {
                        if (getDoctorMapper(sqlSession).getDoctorIdByRoomDateTime(day.getDate(), timeStart, timeEnd, roomId) != doctor.getId()) {
                            roomIsFree = false;
                        }
                    }
                }

                if (roomIsFree) {
                    getUserMapper(sqlSession).insert(doctor, "doctor");
                    getDoctorMapper(sqlSession).insert(doctor, specialityId, roomId);
                    for (DaySchedule daySchedule: schedule) {
                        getScheduleMapper(sqlSession).insertDaySchedule(daySchedule, doctor.getId());
                        List<Slot> slotSchedule = daySchedule.getSlotSchedule();
                        LocalTime timeStart = slotSchedule.get(0).getTimeStart();
                        LocalTime timeEnd = slotSchedule.get(slotSchedule.size() - 1).getTimeEnd();
                        getScheduleMapper(sqlSession).setRoomOccupation(daySchedule.getDate(), timeStart, timeEnd, doctor.getId(), roomId);
                        for (Slot slot: slotSchedule) {
                            getScheduleMapper(sqlSession).insertSlot(slot, daySchedule.getId());
                        }
                    }
                } else {
                    throw new ServerException(new MyError(ServerErrorCode.ROOM_IS_BUSY, Field.ROOM));
                }
            } catch (PersistenceException ex) {
                sqlSession.rollback();
                if (ex.getCause() instanceof MySQLIntegrityConstraintViolationException) {
                    throw new ServerException(new MyError(ServerErrorCode.USER_DUPLICATE, Field.LOGIN));
                } else throw ex;
            } catch (ServerException ex) {
                LOGGER.info("ROOM_IS_BUSY {}: {}", doctor.getRoom(), ex);
                sqlSession.rollback();
                throw ex;
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
    public void insertSchedule(int doctorId, String room, List<DaySchedule> schedule) throws ServerException {
        LOGGER.debug("DAO insert Schedule for doctor with id {}", doctorId);
        int roomId = getRoomId(room);
        boolean roomIsFree = true;
        try (SqlSession sqlSession = getSession()) {
            try {
                for (DaySchedule day: schedule) {
                    List<Slot> slotSchedule = day.getSlotSchedule();
                    LocalTime timeStart = slotSchedule.get(0).getTimeStart();
                    LocalTime timeEnd = slotSchedule.get(slotSchedule.size() - 1).getTimeEnd();
                    int count = getDoctorMapper(sqlSession).getDoctorIdCountByRoomDateTime(day.getDate(), timeStart, timeEnd, roomId);
                    if (count != 0 ) {
                        if (getDoctorMapper(sqlSession).getDoctorIdByRoomDateTime(day.getDate(), timeStart, timeEnd, roomId) != doctorId) {
                            roomIsFree = false;
                        }
                    }
                }
                 if (roomIsFree) {
                     for (DaySchedule daySchedule: schedule) {
                         getScheduleMapper(sqlSession).insertDaySchedule(daySchedule, doctorId);
                         List<Slot> slotSchedule = daySchedule.getSlotSchedule();
                         LocalTime timeStart = slotSchedule.get(0).getTimeStart();
                         LocalTime timeEnd = slotSchedule.get(slotSchedule.size() - 1).getTimeEnd();
                         getScheduleMapper(sqlSession).setRoomOccupation(daySchedule.getDate(), timeStart, timeEnd, doctorId, roomId);
                         for (Slot slot: slotSchedule) {
                             getScheduleMapper(sqlSession).insertSlot(slot, daySchedule.getId());
                         }
                     }
                 } else {
                     throw new ServerException(new MyError(ServerErrorCode.ROOM_IS_BUSY, Field.ROOM));
                 }

            } catch (RuntimeException | ServerException ex) {
                LOGGER.info("Can't insert Schedule for doctor with id {}", doctorId, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void updateSchedule(int doctorId, String room, List<DaySchedule> oldSchedule, List<DaySchedule> schedule) throws ServerException {
        LOGGER.debug("DAO update Schedule for doctor with id {}", doctorId);
        int roomId = getRoomId(room);
        boolean roomIsFree = true;
        try (SqlSession sqlSession = getSession()) {
            try {
                for (DaySchedule day: schedule) {
                    List<Slot> slotSchedule = day.getSlotSchedule();
                    LocalTime timeStart = slotSchedule.get(0).getTimeStart();
                    LocalTime timeEnd = slotSchedule.get(slotSchedule.size() - 1).getTimeEnd();
                    int count = getDoctorMapper(sqlSession).getDoctorIdCountByRoomDateTime(day.getDate(), timeStart, timeEnd, roomId);
                    if (count != 0 ) {
                        if (getDoctorMapper(sqlSession).getDoctorIdByRoomDateTime(day.getDate(), timeStart, timeEnd, roomId) != doctorId) {
                            roomIsFree = false;
                        }
                    }
                }
                if (roomIsFree) {
                    LocalDate startDate = schedule.get(0).getDate();
                    LocalDate endDate = schedule.get(schedule.size() - 1).getDate();
                    if (shouldReplaceSchedule(oldSchedule, startDate, endDate)) {
                        deleteSchedule(doctorId, startDate, endDate, sqlSession);
                        deleteRoomOccupation(doctorId, startDate, endDate, sqlSession);
                        for (DaySchedule daySchedule : schedule) {
                            getScheduleMapper(sqlSession).insertDaySchedule(daySchedule, doctorId);
                            List<Slot> slotSchedule = daySchedule.getSlotSchedule();
                            LocalTime timeStart = slotSchedule.get(0).getTimeStart();
                            LocalTime timeEnd = slotSchedule.get(slotSchedule.size() - 1).getTimeEnd();
                            getScheduleMapper(sqlSession).setRoomOccupation(daySchedule.getDate(), timeStart, timeEnd, doctorId, roomId);
                            for (Slot slot : slotSchedule) {
                                getScheduleMapper(sqlSession).insertSlot(slot, daySchedule.getId());
                            }
                        }
                    } else {
                        throw new ServerException(new MyError(ServerErrorCode.WRONG_SCHEDULE, Field.SCHEDULE));
                    }
                } else {
                    throw new ServerException(new MyError(ServerErrorCode.ROOM_IS_BUSY, Field.ROOM));
                }

            } catch (RuntimeException | ServerException ex) {
                LOGGER.info("Can't insert Schedule for doctor with id {}", doctorId, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

//    @Override
//    public int getSlotIdByDateTime(int doctorId, LocalDate date, LocalTime time) {
//        LOGGER.debug("DAO get Slot id for appointment: doctor id {}, {} {}", doctorId, date, time);
//        try (SqlSession sqlSession = getSession()) {
//            try {
//                return getScheduleMapper(sqlSession).getSlotIdByDateTime(doctorId, date, time);
//            } catch (RuntimeException ex) {
//                LOGGER.info("Can't get Slot id for appointment:  doctor id {} {} {} - {}", doctorId, date, time, ex);
//                throw ex;
//            }
//        }
//    }
//
//    @Override
//    public List<String> getTicketsByDateTimeRange(int doctorId, LocalDate date, LocalTime timeStart, LocalTime timeEnd) {
//        LOGGER.debug("DAO get Ticket numbers for appointment: doctor id {}, {} {} {}", doctorId, date, timeStart, timeEnd);
//        try (SqlSession sqlSession = getSession()) {
//            try {
//                return getScheduleMapper(sqlSession).getTicketsByDateTimeRange(doctorId, date, timeStart, timeEnd);
//            } catch (RuntimeException ex) {
//                LOGGER.info("Can't get Ticket numbers for appointment: doctor id {} {} {} {} - {}", doctorId, date, timeStart, timeEnd, ex);
//                throw ex;
//            }
//        }
//    }

    @Override
    public Doctor getDoctorByUserId(int userId) {
        LOGGER.debug("DAO get Doctor with id: {}", userId);
        User user = getUserById(userId);
        try (SqlSession sqlSession = getSession()) {
            try {
                Doctor doctor = getDoctorMapper(sqlSession).getDoctorByUserId(userId);
                doctor.setFirstName(user.getFirstName());
                doctor.setLastName(user.getLastName());
                doctor.setPatronymic(user.getPatronymic());
                doctor.setLogin(user.getLogin());
                doctor.setPassword(user.getPassword());
                return doctor;
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get Doctor with id {}: {}", userId, ex);
                throw ex;
            }
        }
    }

    @Override
    public int getUserIdByDoctorId(int doctorId) {
        LOGGER.debug("DAO get userId by doctorId {}", doctorId);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getDoctorMapper(sqlSession).getUserIdByDoctorId(doctorId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get userId by doctorId {} - {}", doctorId, ex);
                throw ex;
            }
        }
    }

    @Override
    public Doctor getDoctorWithoutScheduleByDoctorId(int doctorId) {
        LOGGER.debug("DAO get Doctor with doctorId: {}", doctorId);
        try (SqlSession sqlSession = getSession()) {
            try {
                Doctor doctor = getDoctorMapper(sqlSession).getDoctorWithoutScheduleByDoctorId(doctorId);
                User user = getUserById(doctor.getUserId());
                doctor.setFirstName(user.getFirstName());
                doctor.setLastName(user.getLastName());
                doctor.setPatronymic(user.getPatronymic());
                doctor.setLogin(user.getLogin());
                doctor.setPassword(user.getPassword());
                return doctor;
            } catch (RuntimeException ex) {
                LOGGER.info("Can't Doctor with doctorId: {} - {}", doctorId, ex);
                throw ex;
            }
        }
    }

    @Override
    public List<Integer> getSlotIdsByDateTimeRange(int doctorId, LocalDate date, LocalTime timeStart, LocalTime timeEnd) {
        LOGGER.debug("DAO get Slot ids for appointment: doctor id {}, {} {} {}", doctorId, date, timeStart, timeEnd);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getScheduleMapper(sqlSession).getSlotIdsByDateTimeRange(doctorId, date, timeStart, timeEnd);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get Slot id for appointment:  doctor id {} {} {} {} - {}", doctorId, date, timeStart, timeEnd, ex);
                throw ex;
            }
        }
    }

    @Override
    public List<LocalDate> getRoomOccupationDatesByDoctorId(int doctorId, String room) throws ServerException {
        LOGGER.debug("DAO get Room {} Occupation Date By Doctor Id {}", doctorId, room);
        int roomId = getRoomId(room);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getScheduleMapper(sqlSession).getRoomOccupationDatesByDoctorId(doctorId, roomId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get Room {} Occupation Date By Doctor Id {}", doctorId, room, ex);
                throw ex;
            }
        }
    }

    @Override
    public int getDoctorIdByTicketNumber(String ticketNumber) {
        LOGGER.debug("DAO get doctor id by ticket {}", ticketNumber);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getDoctorMapper(sqlSession).getDoctorIdByTicketNumber(ticketNumber);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get doctor id by ticket {} - {}", ticketNumber, ex);
                throw ex;
            }
        }
    }

    @Override
    public List<Integer> getAllDoctorIds() {
        LOGGER.debug("DAO get all Doctor Ids");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getDoctorMapper(sqlSession).getAllDoctorIds();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get all Doctor Ids -", ex);
                throw ex;
            }
        }
    }

    @Override
    public List<Integer> getAllDoctorIdsBySpeciality(String speciality) throws ServerException {
        LOGGER.debug("DAO get all Doctor Ids with Speciality: {}", speciality);
        int specialityId = getSpecialityId(speciality);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getDoctorMapper(sqlSession).getAllDoctorIdsBySpeciality(specialityId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get all Doctor Ids with Speciality: {} - {}", speciality, ex);
                throw ex;
            }
        }
    }

    @Override
    public void insertCommission(LocalTime timeStart, LocalTime timeEnd, String room, String ticketNumber, int patientId) {
        LOGGER.debug("DAO insert Commission {}", ticketNumber);
        try (SqlSession sqlSession = getSession()) {
            try {
                getDoctorMapper(sqlSession).insertCommission(timeStart, timeEnd, room, ticketNumber, patientId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Commission {} - {}", ticketNumber, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public String getRoomByCommissionTicket(String ticketNumber) {
        LOGGER.debug("DAO get Room by commission ticket {}", ticketNumber);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getScheduleMapper(sqlSession).getRoomByCommissionTicket(ticketNumber);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get Room by commission ticket {} - {}", ticketNumber, ex);
                throw ex;
            }
        }
    }

    @Override
    public List<Slot> deleteScheduleFromDate(int doctorId, LocalDate startDate) {
        LOGGER.debug("DAO delete schedule for doctor with id {} from {}", doctorId, startDate);
        List<Slot> deletedTickets;
        try (SqlSession sqlSession = getSession()) {
            try {
                deletedTickets = getScheduleMapper(sqlSession).getTicketsAfterDate(doctorId, startDate);
                getScheduleMapper(sqlSession).deleteScheduleFromDate(doctorId, startDate);
                deleteRoomOccupationFromDate(doctorId, startDate, sqlSession);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete schedule for doctor with id {} from {} - {}", doctorId, startDate, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return deletedTickets;
    }

//    @Override
//    public void deleteRoomOccupationFromDate(int doctorId, LocalDate startDate) {
//        LOGGER.debug("DAO delete occupation for doctor with id {} from {}", doctorId, startDate);
//        try (SqlSession sqlSession = getSession()) {
//            try {
//                getDoctorMapper(sqlSession).deleteRoomOccupation(doctorId, startDate);
//            } catch (RuntimeException ex) {
//                LOGGER.info("Can't delete occupation for doctor with id {} from {}- {}", doctorId, startDate, ex);
//                sqlSession.rollback();
//                throw ex;
//            }
//            sqlSession.commit();
//        }
//    }

    @Override
    public void deleteCommission(String ticketNumber) {
        LOGGER.debug("DAO delete commission with ticket number {}", ticketNumber);
        try (SqlSession sqlSession = getSession()) {
            try {
                getDoctorMapper(sqlSession).deleteCommission(ticketNumber);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete commission with ticket number {} - {}", ticketNumber, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public int getDoctorAppointmentsNumber(int doctorId, LocalDate startDate, LocalDate endDate) {
        LOGGER.debug("DAO get number of patients by doctor with id {} within {} - {}", doctorId, startDate, endDate);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getScheduleMapper(sqlSession).getPatientsByDoctorDateRange(doctorId, startDate, endDate).size();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get number of patients by doctor with id {} within {} - {}: {}", doctorId, startDate, endDate, ex);
                throw ex;
            }
        }
    }

    @Override
    public int getWorkingMinutesBySchedule(int doctorId, LocalDate startDate, LocalDate endDate) {
        LOGGER.debug("DAO get working hours by doctor with id {} within {} - {}", doctorId, startDate, endDate);
        try (SqlSession sqlSession = getSession()) {
            try {
                List<Integer> minutesPerDay = getScheduleMapper(sqlSession).getWorkingMinutesByDoctorBySchedule(doctorId, startDate, endDate);
                return minutesPerDay.stream().reduce(0, Integer::sum);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get working hours by doctor with id {} within {} - {}: {}", doctorId, startDate, endDate, ex);
                throw ex;
            }
        }
    }

}

