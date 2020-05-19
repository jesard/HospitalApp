package net.thumbtack.school.hospital;

import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestDoctorDao extends TestBase {

    @Test
    public void testGetSpecialityById() {
        String speciality1 = "therapist";
        String speciality2 = "plumber";
        assertAll(
                () -> assertTrue(doctorDao.getSpecialityId(speciality1) > 0),
                () -> assertThrows(ServerException.class, ()-> doctorDao.getSpecialityId(speciality2))
        );


    }

    @Test
    public void testGetRoomIdByName() {
        String room1 = "111";
        String room2 = "222";
        assertAll(
                () -> assertTrue(doctorDao.getRoomId(room1) > 0),
                () -> assertThrows(ServerException.class, ()-> doctorDao.getRoomId(room2))
        );
    }

    @Test
    public void testGetRoomIdByDoctorId() throws ServerException {
        Doctor doctor = insertDoctorWithoutSchedule();
        assertAll(
                () -> assertTrue(doctorDao.getRoomId(doctor.getId()) > 0),
                () -> assertThrows(ServerException.class, ()-> doctorDao.getRoomId(0))
        );
    }

    @Test
    public void testDoctorInsertWithoutSchedule() throws ServerException {
        Doctor doctor1 = insertDoctorWithoutSchedule();
        Doctor doctorFromDb = doctorDao.getDoctorByUserId(doctor1.getUserId());
        assertEquals(doctor1, doctorFromDb);
    }

    @Test
    public void testDoctorInsertWithSchedule1() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        Doctor doctorFromDb = doctorDao.getDoctorByUserId(doctor.getUserId());
        assertAll(
                () -> assertEquals(doctor.getLastName(), doctorFromDb.getLastName()),
                () -> assertEquals(doctor.getSchedule().size(), doctorFromDb.getSchedule().size())
        );
    }

    @Test
    public void testInsertSchedule() throws ServerException {
        Doctor doctor = insertDoctorWithoutSchedule();
        List<DaySchedule> schedule = makeSchedule1();
        doctorDao.insertSchedule(doctor.getId(), doctor.getRoom(), schedule);
        Doctor doctorFromDb = doctorDao.getDoctorByUserId(doctor.getUserId());
        assertEquals(schedule.get(0).getDate(), doctorFromDb.getSchedule().get(0).getDate());
    }

    @Test
    public void testUpdateSchedule() {
        //расписания не пересекаются
        //пересекаются без приемов
        //пересекаются с приемами
    }

    @Test
    public void testGetUserIdByDoctorId() {}

    @Test
    public void testGetDoctorWithoutScheduleByDoctorId() {
        //док c расписанием
        //док без расписания
    }

    @Test
    public void testGetSlotIdsByDateTimeRange() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        List<Integer> slotIds = Arrays.asList(
                doctor.getSchedule().get(0).getSlotSchedule().get(0).getId(),
                doctor.getSchedule().get(0).getSlotSchedule().get(1).getId());
        List<Integer> slotIdsFromDb = doctorDao.getSlotIdsByDateTimeRange(
                doctor.getId(),
                doctor.getSchedule().get(0).getDate(),
                doctor.getSchedule().get(0).getSlotSchedule().get(0).getTimeStart().plusMinutes(1),
                doctor.getSchedule().get(0).getSlotSchedule().get(1).getTimeEnd().plusMinutes(10));
        assertEquals(slotIdsFromDb, slotIds);
    }

    @Test
    public void testGetDoctorIdByTicketNumber() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        Patient patient = insertPatient1();
        int slotId = doctor.getSchedule().get(0).getSlotSchedule().get(0).getId();
        String ticketNumber =  "TestTicketNumber1";
        patientDao.makeAppointment(patient.getId(), slotId, ticketNumber);
        int doctorIdFromDb = doctorDao.getDoctorIdByTicketNumber("TestTicketNumber1");
        assertEquals(doctor.getId(), doctorIdFromDb);
    }

    @Test
    public void testGetAllDoctorIds() throws ServerException {
        Doctor doctor1 = insertDoctorWithSchedule1();
        Doctor doctor2 = insertDoctorWithSchedule2();
        List<Integer> doctorIds = new ArrayList<>();
        doctorIds.add(doctor1.getId());
        doctorIds.add(doctor2.getId());
        List<Integer> doctorIdsFromDb = doctorDao.getAllDoctorIds();
        assertEquals(doctorIds, doctorIdsFromDb);
    }

    @Test
    public void testGetAllDoctorIdsBySpec() throws ServerException {
        Doctor doctor1 = insertDoctorWithSchedule1();
        Doctor doctor2 = insertDoctorWithSchedule2();
        List<Integer> doctorIds = new ArrayList<>();
        doctorIds.add(doctor1.getId());
        List<Integer> doctorIdsFromDb = doctorDao.getAllDoctorIdsBySpeciality(doctor1.getSpeciality());
        assertEquals(doctorIds, doctorIdsFromDb);
    }

    @Test
    public void testInsertCommission() throws ServerException {
        Doctor doctor1 = insertDoctorWithSchedule1();
        Doctor doctor2 = insertDoctorWithSchedule2();
        Patient patient = insertPatient1();
        String ticketNumber = "TestTicketNumber1";
        doctorDao.insertCommission(
                LocalTime.of(10,30),
                LocalTime.of(11,0),
                doctor1.getRoom(),
                ticketNumber,
                patient.getId());
        String roomFromDb = doctorDao.getRoomByCommissionTicket(ticketNumber);
        assertEquals(doctor1.getRoom(), roomFromDb);
    }

    @Test
    public void testDeleteScheduleFromDate() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        List<DaySchedule> schedule = doctor.getSchedule();
        LocalDate startDate = schedule.get(1).getDate();
        doctorDao.deleteScheduleFromDate(doctor.getId(), startDate);
        Doctor doctorFromDb = doctorDao.getDoctorByUserId(doctor.getUserId());
        List<DaySchedule> scheduleFromDb = doctorFromDb.getSchedule();
        assertAll(
                () -> assertTrue(schedule.size() > scheduleFromDb.size()),
                () -> assertTrue(
                    schedule.get(schedule.size() - 1).getDate()
                    .isAfter(scheduleFromDb.get(scheduleFromDb.size() - 1).getDate())),
                () -> assertFalse(
                    doctorDao.getRoomOccupationDatesByDoctorId(doctor.getId(),
                    doctor.getRoom()).contains(startDate))
        );
    }

    @Test
    public void testDeleteScheduleWithTicketsFromDate() {}

    @Test
    public void testDeleteCommission() throws ServerException {
        Doctor doctor1 = insertDoctorWithSchedule1();
        Doctor doctor2 = insertDoctorWithSchedule2();
        Patient patient = insertPatient1();
        String ticketNumber = "TestTicketNumber1";
        doctorDao.insertCommission(
                LocalTime.of(10,30),
                LocalTime.of(11,0),
                doctor1.getRoom(),
                ticketNumber,
                patient.getId());
        doctorDao.deleteCommission(ticketNumber);
        assertNull(doctorDao.getRoomByCommissionTicket(ticketNumber));
    }

    @Test
    public void testGetDoctorAppointmentsNumber() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        Patient patient1 = insertPatient1();
        Patient patient2 = insertPatient2();
        List<DaySchedule> schedule = doctor.getSchedule();
        LocalDate startDate = schedule.get(0).getDate();
        LocalDate endDate = schedule.get(schedule.size() - 1).getDate();
        int slotId1 = schedule.get(0).getSlotSchedule().get(0).getId();
        int slotId2 = schedule.get(0).getSlotSchedule().get(1).getId();
        String ticketNumber1 =  "TestTicketNumber1";
        String ticketNumber2 =  "TestTicketNumber2";
        patientDao.makeAppointment(patient1.getId(), slotId1, ticketNumber1);
        patientDao.makeAppointment(patient2.getId(), slotId2, ticketNumber2);
        assertEquals(2, doctorDao.getDoctorAppointmentsNumber(doctor.getId(), startDate, endDate));
    }

    @Test
    public void testGetWorkingHoursBySchedule() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        List<DaySchedule> schedule = doctor.getSchedule();
        LocalDate startDate = schedule.get(0).getDate();
        LocalDate endDate = schedule.get(schedule.size() - 1).getDate();
        int workMinutes = 0;
        for (DaySchedule day: schedule) {
             int minutesPerSlot = (day.getSlotSchedule().get(0).getTimeEnd().toSecondOfDay()
                     - day.getSlotSchedule().get(0).getTimeStart().toSecondOfDay())
                     / 60;
             int minutesPerDay = day.getSlotSchedule().size() * minutesPerSlot;
             workMinutes += minutesPerDay;
        }
        assertEquals(workMinutes, doctorDao.getWorkingMinutesBySchedule(doctor.getId(), startDate, endDate));


    }


}
