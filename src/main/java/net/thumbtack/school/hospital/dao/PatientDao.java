package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.Slot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PatientDao extends UserDao {

    Patient insertPatient(Patient patient) throws ServerException;

    int getUserIdByPatientId(int patientId) throws ServerException;

    Patient getPatientByUserId(int id) throws ServerException;

//    void makeAppointment(int patientId, List<Integer> slotIds, String ticketNumber) throws ServerException;

    void makeAppointments(int patientId, List<Integer> slotIds, String ticketNumber) throws ServerException;

    int makeAppointment(int patientId, int slotId, String ticketNumber) throws ServerException;

    List<Slot> getBusySlotsByPatientIdDate(int patientId, LocalDate date);

    void updatePatient(Patient patient);

    void deleteTicket(String ticketNumber);

    int getPatientIdByTicketNumber(String ticketNumber) throws ServerException;

    int getPatientAppointmentsNumber(int patientId, LocalDate startDate, LocalDate endDate);
}
