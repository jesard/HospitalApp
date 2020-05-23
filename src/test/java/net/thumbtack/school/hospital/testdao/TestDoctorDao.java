package net.thumbtack.school.hospital.testdao;

import net.thumbtack.school.hospital.TestBase;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.Slot;
import org.junit.jupiter.api.Test;

import javax.print.Doc;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
    public void testUpdateScheduleNotIntersects() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        List<DaySchedule> oldSchedule = doctor.getSchedule();
        LocalDate startDate = oldSchedule.get(oldSchedule.size() - 1).getDate().plusDays(1);
        LocalDate endDate = startDate.plusDays(2);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(12, 0);
        int minDuration = 10;
        int maxDuration = 30;
        int duration = ThreadLocalRandom.current().nextInt(minDuration, maxDuration + 1);
        List<DaySchedule> newSchedule = makeScheduleSameEveryDay(startDate, endDate, startTime, endTime, duration);
        doctorDao.updateSchedule(doctor.getId(), doctor.getRoom(), oldSchedule, newSchedule);
        Doctor doctorWithNewSchedule = doctorDao.getDoctorByUserId(doctor.getUserId());
        assertEquals(doctorWithNewSchedule.getSchedule().size(), oldSchedule.size() + newSchedule.size());

    }

    @Test
    public void testDateOrderInSchedule() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        List<DaySchedule> oldSchedule = doctor.getSchedule();
        LocalDate startDate = oldSchedule.get(0).getDate().minusDays(2);
        LocalDate endDate = oldSchedule.get(0).getDate();
        LocalTime startTime = LocalTime.of(16, 0);
        LocalTime endTime = LocalTime.of(18, 0);
        int minDuration = 10;
        int maxDuration = 30;
        int duration = ThreadLocalRandom.current().nextInt(minDuration, maxDuration + 1);
        List<DaySchedule> newSchedule = makeScheduleSameEveryDay(startDate, endDate, startTime, endTime, duration);
        doctorDao.updateSchedule(doctor.getId(), doctor.getRoom(), oldSchedule, newSchedule);
        Doctor doctorWithNewSchedule = doctorDao.getDoctorByUserId(doctor.getUserId());
        List<DaySchedule> newScheduleFromDb = doctorWithNewSchedule.getSchedule();
        assertTrue(doctorWithNewSchedule.getSchedule().get(0).getDate()
                .isBefore(doctorWithNewSchedule.getSchedule().get(1).getDate()));
    }

    @Test
    public void testUpdateScheduleWithIntersection() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        List<DaySchedule> oldSchedule = doctor.getSchedule();
        DaySchedule lastInOldSchedule = oldSchedule.get(oldSchedule.size() - 1);
        LocalDate startDate = lastInOldSchedule.getDate();
        LocalDate endDate = startDate.plusDays(2);
        LocalTime startTime = lastInOldSchedule.getSlotSchedule().get(0).getTimeStart().plusHours(1);
        LocalTime endTime = startTime.plusHours(2);
        int minDuration = 10;
        int maxDuration = 30;
        int duration = ThreadLocalRandom.current().nextInt(minDuration, maxDuration + 1);
        List<DaySchedule> newSchedule = makeScheduleSameEveryDay(startDate, endDate, startTime, endTime, duration);
        doctorDao.updateSchedule(doctor.getId(), doctor.getRoom(), oldSchedule, newSchedule);
        Doctor doctorWithNewSchedule = doctorDao.getDoctorByUserId(doctor.getUserId());
        LocalTime startTimeFromDb = null;
        for (DaySchedule day: doctorWithNewSchedule.getSchedule()) {
            if (day.getDate().equals(startDate)) {
                startTimeFromDb = day.getSlotSchedule().get(0).getTimeStart();
            }
        }
        assertEquals(startTime, startTimeFromDb);


    }

    @Test
    public void testUpdateScheduleWithIntersectionWithTickets() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        Patient patient = insertPatient1();
        int slotId = doctor.getSchedule().get(doctor.getSchedule().size() - 1).getSlotSchedule().get(0).getId();
        String ticketNumber =  "TestTicketNumber1";
        patientDao.makeAppointment(patient.getId(), slotId, ticketNumber);
        doctor = doctorDao.getDoctorByUserId(doctor.getUserId());
        List<DaySchedule> oldSchedule = doctor.getSchedule();
        DaySchedule lastInOldSchedule = oldSchedule.get(oldSchedule.size() - 1);

        LocalDate intersectDate = lastInOldSchedule.getDate();
        LocalDate endDate = intersectDate.plusDays(2);
        LocalTime newStartTime = lastInOldSchedule.getSlotSchedule().get(0).getTimeStart().plusHours(1);
        LocalTime newEndTime = newStartTime.plusHours(2);
        int minDuration = 10;
        int maxDuration = 30;
        int duration = ThreadLocalRandom.current().nextInt(minDuration, maxDuration + 1);
        List<DaySchedule> newSchedule = makeScheduleSameEveryDay(intersectDate, endDate, newStartTime, newEndTime, duration);
        try {
            doctorDao.updateSchedule(doctor.getId(), doctor.getRoom(), oldSchedule, newSchedule);
        } catch (Exception ex) {
            assertTrue(ex instanceof ServerException);
        }
        Doctor doctorWithNewSchedule = doctorDao.getDoctorByUserId(doctor.getUserId());
        LocalTime startTimeFromDb = null;
        for (DaySchedule day: doctorWithNewSchedule.getSchedule()) {
            if (day.getDate().equals(intersectDate)) {
                startTimeFromDb = day.getSlotSchedule().get(0).getTimeStart();
            }
        }
        LocalTime oldStartTime = lastInOldSchedule.getSlotSchedule().get(0).getTimeStart();
        assertEquals(oldStartTime, startTimeFromDb);
    }

    @Test
    public void testGetUserIdByDoctorId() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        int userIdFromDb = doctorDao.getUserIdByDoctorId(doctor.getId());
        assertEquals(doctor.getUserId(), userIdFromDb);
    }

    @Test
    public void testGetDoctorWithoutScheduleByDoctorId() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        Doctor doctorFromDb = doctorDao.getDoctorWithoutScheduleByDoctorId(doctor.getId());
        assertEquals(0, doctorFromDb.getSchedule().size());
        assertEquals(doctor.getLastName(), doctorFromDb.getLastName());
    }

    @Test
    public void testGetSlotIdByDateTime() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        int slotId = doctor.getSchedule().get(0).getSlotSchedule().get(0).getId();
        int slotIdFromDb = doctorDao.getSlotIdByDateTime(
                doctor.getId(),
                doctor.getSchedule().get(0).getDate(),
                doctor.getSchedule().get(0).getSlotSchedule().get(0).getTimeStart());
        assertEquals(slotId, slotIdFromDb);
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
    public void testDeleteScheduleWithTicketsFromDate() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        List<DaySchedule> schedule = doctor.getSchedule();
        Patient patient = insertPatient1();
        int slotId = schedule.get(0).getSlotSchedule().get(0).getId();
        String ticketNumber =  "TestTicketNumber1";
        patientDao.makeAppointment(patient.getId(), slotId, ticketNumber);

        LocalDate startDate = schedule.get(0).getDate();
        List<Slot> deletedTickets = doctorDao.deleteScheduleFromDate(doctor.getId(), startDate);
        assertEquals(1, deletedTickets.size());
        assertEquals(ticketNumber, deletedTickets.get(0).getTicketNumber());
    }

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
