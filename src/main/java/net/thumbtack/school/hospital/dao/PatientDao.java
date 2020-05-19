package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.Patient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PatientDao extends UserDao {

    Patient insertPatient(Patient patient);

    int getUserIdByPatientId(int patientId);

    Patient getPatientByUserId(int id);

//    void makeAppointment(int patientId, List<Integer> slotIds, String ticketNumber) throws ServerException;

    int makeAppointment(int patientId, int slotId, String ticketNumber) throws ServerException;

    int getSlotIdByDateTime(int doctorId, LocalDate date, LocalTime timeStart);

    void updatePatient(Patient patient);

    void deleteTicket(String ticketNumber);

    int getPatientIdByTicketNumber(String ticketNumber);

    int getPatientAppointmentsNumber(int patientId, String startDate, String endDate);
}
