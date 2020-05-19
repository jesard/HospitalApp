package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.request.MakeAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegPatientDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.*;
import net.thumbtack.school.hospital.dto.response.regdoctor.RegDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.response.regdoctor.TicketDtoResponse;
import net.thumbtack.school.hospital.error.Field;
import net.thumbtack.school.hospital.error.MyError;
import net.thumbtack.school.hospital.error.ServerErrorCode;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.Slot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PatientService extends UserService {

    protected Patient makePatientFromDtoRequest(RegPatientDtoRequest request) {
        Patient patient = new Patient();
        makeUserFromDtoRequest(patient, request);
        patient.setAddress(request.getAddress());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone().replace("-", ""));
        return patient;
    }

    public RegPatientDtoResponse registerPatient(RegPatientDtoRequest request) {
        Patient patient = makePatientFromDtoRequest(request);
        patient.setPhone(patient.getPhone().replace("-", ""));
        patientDao.insertPatient(patient);
        return new RegPatientDtoResponse(patient.getId(),
                patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                patient.getEmail(), patient.getAddress(), patient.getPhone());
    }

//    public Patient getPatientByPatientId(int patientId) {
//        int userId = patientDao.getUserIdByPatientId(patientId);
//        return patientDao.getPatientByUserId(userId);
//    }

    public RegPatientDtoResponse getPatientInfoByPatientId(int patientId, String token) throws ServerException {
        if (getUserDecriptorByToken(token).equals(PATIENT)) {
            throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
        }
        int userId = patientDao.getUserIdByPatientId(patientId);
        return (RegPatientDtoResponse) getUserDtoResponseByUserId(userId);
    }

    public RegPatientDtoResponse updatePatient(UpdatePatientDtoRequest request, String token) throws ServerException {
        int userId = userDao.getUserIdByToken(token);
        Patient patient = patientDao.getPatientByUserId(userId);
        if(patient.getPassword().equals(request.getOldPassword())) {
            patient.setFirstName(request.getFirstName());
            patient.setLastName(request.getLastName());
            patient.setPatronymic(request.getPatronymic());
            patient.setEmail(request.getEmail());
            patient.setAddress(request.getAddress());
            patient.setPhone(request.getPhone());
            patient.setPassword(request.getNewPassword());
            patientDao.updatePatient(patient);
            return new RegPatientDtoResponse(patient.getId(), request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getEmail(), request.getAddress(), request.getPhone());
        }
        throw new ServerException(new MyError(ServerErrorCode.WRONG_PASSWORD, Field.PASSWORD));
    }

    public MakeAppointmentDtoResponse makeAppointment(MakeAppointmentDtoRequest request, String token) throws ServerException {
        if (!getUserDecriptorByToken(token).equals(PATIENT)) {
            throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
        }
        LocalDate localDate = LocalDate.parse(request.getDate(), formatterDate);
        if (localDate.isAfter(LocalDate.now().plusMonths(2))) {
            throw new ServerException(new MyError(ServerErrorCode.WRONG_DATE, Field.DATE));
        }
        int userId = userDao.getUserIdByToken(token);
        Patient patient = patientDao.getPatientByUserId(userId);
        String speciality = request.getSpeciality();
        int doctorId;
        if (speciality != null && speciality.length() != 0) {
            List<Integer> doctorIds = doctorDao.getAllDoctorIdsBySpeciality(speciality);
            doctorId = doctorIds.get(new Random().nextInt(doctorIds.size()));
        } else {
            doctorId = request.getDoctorId();
        }
        Doctor doctor = doctorDao.getDoctorWithoutScheduleByDoctorId(doctorId);
        String ticketNumber =  "D" + doctorId + request.getDate().replace("-","") + request.getTime().replace(":", "");
        LocalDate date = LocalDate.parse(request.getDate(), formatterDate);
        LocalTime timeStart = LocalTime.parse(request.getTime(), formatterTime);
        int slotId = patientDao.getSlotIdByDateTime(doctorId, date, timeStart);
        int updatedRows = patientDao.makeAppointment(patient.getId(), slotId, ticketNumber);
        if (updatedRows == 0) {
            throw new ServerException(new MyError(ServerErrorCode.SLOT_IS_BUSY, Field.DATETIME));
        }
        String subject = "Ticket " + ticketNumber + "created at " + request.getDate() + " " + request.getTime() + "in room " + doctor.getRoom();
        sendEmail(patient.getAddress(), subject, subject);
        sendSMS(patient.getPhone(), subject, subject);
        return new MakeAppointmentDtoResponse(ticketNumber, doctorId, doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getSpeciality(), doctor.getRoom(), request.getDate(), request.getTime());
    }

    public EmptyJsonResponse deleteCommission(String ticketNumber, String token) throws ServerException {
        int userId = userDao.getUserIdByToken(token);
        Patient patient = patientDao.getPatientByUserId(userId);
        for (Slot slot: patient.getCommissions()) {
            if (slot.getTicketNumber().equals(ticketNumber)) {
                patientDao.deleteTicket(ticketNumber);
            }
        }
        doctorDao.deleteCommission(ticketNumber);
        return new EmptyJsonResponse();
    }

    public GetTicketsDtoResponse getTickets(String token) throws ServerException {
        int userId = userDao.getUserIdByToken(token);
        Patient patient = patientDao.getPatientByUserId(userId);
        List<Slot> tickets = patient.getTickets();
        List<Slot> commissions = patient.getCommissions();
        List<TicketDtoResponse> ticketsDtoResponse = new ArrayList<>();
        for (Slot slot: tickets) {
            String ticketNumber = slot.getTicketNumber();
            int length = ticketNumber.length();
            int docId = Integer.parseInt(ticketNumber.substring(0, length - 12).replace("D", ""));
            Doctor doc = doctorDao.getDoctorWithoutScheduleByDoctorId(docId);
            String date = ticketNumber.substring(length - 12, length - 10) + "-" + ticketNumber.substring(length - 10, length - 8) + "-" + ticketNumber.substring(length - 8, length - 4);
            String time = ticketNumber.substring(length - 4, length - 2) + ":" + ticketNumber.substring(length - 2, length);
            RegDoctorDtoResponse regDoctorDtoResponse = new RegDoctorDtoResponse(doc.getId(), doc.getFirstName(), doc.getLastName(), doc.getPatronymic(), doc.getSpeciality());
            TicketDtoResponse ticketDtoResponse = new TicketDtoResponse(slot.getTicketNumber(), doc.getRoom(), date, time, regDoctorDtoResponse);
            ticketsDtoResponse.add(ticketDtoResponse);
        }
        for (Slot slot: commissions) {
            String ticketNumber = slot.getTicketNumber();
            int length = ticketNumber.length();
            String date = ticketNumber.substring(length - 12, length - 10) + "-" + ticketNumber.substring(length - 10, length - 8) + "-" + ticketNumber.substring(length - 8, length - 4);
            String time = ticketNumber.substring(length - 4, length - 2) + ":" + ticketNumber.substring(length - 2, length);
            String[] docIds = ticketNumber.substring(0, length - 12).replace("CD", "").split("D");
            List<RegDoctorDtoResponse> docList = new ArrayList<>();
            for (String docId: docIds) {
                Doctor doc = doctorDao.getDoctorWithoutScheduleByDoctorId(Integer.parseInt(docId));
                RegDoctorDtoResponse regDoctorDtoResponse = new RegDoctorDtoResponse(doc.getId(), doc.getFirstName(), doc.getLastName(), doc.getPatronymic(), doc.getSpeciality());
                docList.add(regDoctorDtoResponse);
            }
            String room = doctorDao.getRoomByCommissionTicket(ticketNumber);
            TicketDtoResponse ticketDtoResponse = new TicketDtoResponse(slot.getTicketNumber(), room, date, time, docList);
            ticketsDtoResponse.add(ticketDtoResponse);
        }
        return new GetTicketsDtoResponse(ticketsDtoResponse);
    }


}
