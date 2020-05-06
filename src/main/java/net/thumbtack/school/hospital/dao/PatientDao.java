package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.Patient;

import java.time.LocalDate;
import java.time.LocalTime;

public interface PatientDao extends UserDao {

    Patient insertPatient(Patient patient);

    Patient getPatientByUserId(int id);

    void makeAppointment(Patient patient, int slotId, String ticketNumber);

    int getSlotIdByDateTime(int doctorId, LocalDate date, LocalTime timeStart);
}
