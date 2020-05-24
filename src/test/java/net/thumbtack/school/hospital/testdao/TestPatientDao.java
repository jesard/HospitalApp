package net.thumbtack.school.hospital.testdao;

import net.thumbtack.school.hospital.TestBase;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestPatientDao extends TestBase {

    @Test
    public void testInsertPatient() throws ServerException {
        Patient patient = insertPatient1();
        Patient patientFromDb = patientDao.getPatientByUserId(patient.getUserId());
        assertEquals(patient, patientFromDb);
    }

    @Test
    public void testGetUserIdByPatientId() throws ServerException {
        Patient patient = insertPatient1();
        int userIdFromDb = patientDao.getUserIdByPatientId(patient.getId());
        assertEquals(patient.getUserId(), userIdFromDb);
    }

    @Test
    public void testGetPatientWithoutTicketsByUserId() throws ServerException {
        Patient patient = insertPatient1();
        Patient patientFromDb = patientDao.getPatientByUserId(patient.getUserId());
        assertEquals(patient, patientFromDb);
    }

    @Test
    public void testGetPatientWithTicketsByUserId() throws ServerException {
        Patient patient = insertPatient1();
        Doctor doctor = insertDoctorWithSchedule1();
        int slotId = doctor.getSchedule().get(0).getSlotSchedule().get(0).getId();
        String ticketNumber =  "TestTicketNumber1";
        patientDao.makeAppointment(patient.getId(), slotId, ticketNumber);
        Patient patientFromDb = patientDao.getPatientByUserId(patient.getUserId());
        assertEquals(patient.getEmail(), patientFromDb.getEmail());
        assertEquals(patientFromDb.getTickets().get(0).getTicketNumber(), ticketNumber);
    }

    @Test
    public void testUpdatePatient() throws ServerException {
        Patient patient = insertPatient1();
        patient.setFirstName("НовоеИмя");
        patient.setLastName("Фамилия");
        patient.setPatronymic("Отчество");
        patient.setPassword("newPassword");
        patient.setEmail("new@new.com");
        patient.setAddress("newLF:ELF");
        patient.setPhone("232423423");
        patientDao.updatePatient(patient);
        Patient patientFromDb = patientDao.getPatientByUserId(patient.getUserId());
        assertEquals(patient, patientFromDb);
    }

    @Test
    public void testMakeAppointmentFreeSlot() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        Patient patient = insertPatient1();
        int slotId = doctor.getSchedule().get(0).getSlotSchedule().get(0).getId();
        String ticketNumber =  "TestTicketNumber1";
        patientDao.makeAppointment(patient.getId(), slotId, ticketNumber);
        assertEquals(patient.getId(), patientDao.getPatientIdByTicketNumber(ticketNumber));
    }

    @Test
    public void testMakeAppointmentBusySlot() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        Patient patient1 = insertPatient1();
        Patient patient2 = insertPatient2();
        int slotId = doctor.getSchedule().get(0).getSlotSchedule().get(0).getId();
        String ticketNumber1 =  "TestTicketNumber1";
        String ticketNumber2 =  "TestTicketNumber2";
        patientDao.makeAppointment(patient1.getId(), slotId, ticketNumber1);
        assertThrows(ServerException.class, ()-> patientDao.makeAppointment(patient2.getId(), slotId, ticketNumber2));
    }


    @Test
    public void testDeleteTicket() throws ServerException {
        Doctor doctor = insertDoctorWithSchedule1();
        Patient patient = insertPatient1();
        int slotId = doctor.getSchedule().get(0).getSlotSchedule().get(0).getId();
        String ticketNumber =  "TestTicketNumber1";
        patientDao.makeAppointment(patient.getId(), slotId, ticketNumber);
        patientDao.deleteTicket(ticketNumber);
        assertThrows(ServerException.class, ()-> patientDao.getPatientIdByTicketNumber(ticketNumber));
    }

    @Test
    public void testGetPatientAppointmentsNumber() throws ServerException {
        Doctor doctor1 = insertDoctorWithSchedule1();
        Doctor doctor2 = insertDoctorWithSchedule2();
        Patient patient = insertPatient1();
        int slotId1 = doctor1.getSchedule().get(0).getSlotSchedule().get(0).getId();
        int slotId2 = doctor2.getSchedule().get(0).getSlotSchedule().get(0).getId();
        String ticketNumber1 =  "TestTicketNumber1";
        String ticketNumber2 =  "TestTicketNumber2";
        patientDao.makeAppointment(patient.getId(), slotId1, ticketNumber1);
        patientDao.makeAppointment(patient.getId(), slotId2, ticketNumber2);
        LocalDate date1 = doctor1.getSchedule().get(0).getDate();
        LocalDate date2 = doctor2.getSchedule().get(0).getDate();
        int number;
        if (date1.isAfter(date2)) {
            number = patientDao.getPatientAppointmentsNumber(patient.getId(), date2, date1);
        } else {
            number = patientDao.getPatientAppointmentsNumber(patient.getId(), date1, date2);
        }
        assertEquals(2, number);
    }





}
