package net.thumbtack.school.hospital;

import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPatientDao extends TestBase {

    @Test
    public void testInsertPatient() {
        Patient patient = insertPatient1();
        Patient patientFromDb = patientDao.getPatientByUserId(patient.getUserId());
        assertEquals(patient, patientFromDb);
    }

    @Test
    public void testGetUserIdByPatientId() {
        Patient patient = insertPatient1();
        int userIdFromDb = patientDao.getUserIdByPatientId(patient.getId());
        assertEquals(patient.getUserId(), userIdFromDb);
    }

    @Test
    public void testGetPatientWithoutTicketsByUserId() {
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
    public void testUpdatePatient() {
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
    public void testMakeAppointment() {

    }



}
