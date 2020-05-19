package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Slot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorDao extends UserDao {

    int getSpecialityId(String speciality) throws ServerException;

    int getRoomId(String room) throws ServerException;

    int getRoomId(int doctorId) throws ServerException;

    Doctor insertDoctor(Doctor doctor) throws ServerException;

    void insertSchedule(int doctorId, String room, List<DaySchedule> schedule) throws ServerException;

//    int getSlotIdByDateTime(int doctorId, LocalDate date, LocalTime time);
//
//    List<String> getTicketsByDateTimeRange(int doctorId, LocalDate date, LocalTime timeStart, LocalTime timeEnd);

    List<Integer> getSlotIdsByDateTimeRange(int doctorId, LocalDate date, LocalTime timeStart, LocalTime timeEnd);

    String getRoomByCommissionTicket(String ticketNumber);

    List<Slot> deleteScheduleFromDate(int doctorId, LocalDate startDate);

    Doctor getDoctorByUserId(int id);

    int getUserIdByDoctorId(int doctorId);

    List<LocalDate> getRoomOccupationDatesByDoctorId(int doctorId, String room) throws ServerException;

    int getDoctorIdByTicketNumber(String ticketNumber);

    Doctor getDoctorWithoutScheduleByDoctorId(int doctorId);

    List<Integer> getAllDoctorIds();

    List<Integer> getAllDoctorIdsBySpeciality(String speciality) throws ServerException;

//    void deleteRoomOccupationFromDate(int doctorId, LocalDate startDate);

    void insertCommission(LocalTime time, LocalTime timeEnd, String room, String ticketNumber, int patientId);

    void deleteCommission(String ticketNumber);

    void updateSchedule(int doctorId, String room, List<DaySchedule> oldSchedule, List<DaySchedule> newSchedule) throws ServerException;

    int getDoctorAppointmentsNumber(int doctorId, LocalDate startDate, LocalDate endDate);

    int getWorkingMinutesBySchedule(int doctorId, LocalDate startDate, LocalDate endDate);
}
