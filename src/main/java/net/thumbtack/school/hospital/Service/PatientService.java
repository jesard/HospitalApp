package net.thumbtack.school.hospital.Service;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Preconditions;
import net.thumbtack.school.hospital.dao.PatientDao;
import net.thumbtack.school.hospital.daoimpl.PatientDaoImpl;
import net.thumbtack.school.hospital.dto.request.MakeAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegPatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.MakeAppointmentDtoResponse;
import net.thumbtack.school.hospital.dto.response.RegPatientDtoResponse;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PatientService extends UserService {

    protected static Patient makePatientFromDtoRequest(RegPatientDtoRequest request) {
        Patient patient = new Patient();
        UserService.makeUserFromDtoRequest(patient, request);
        patient.setAddress(request.getAddress());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        return patient;
    }

    protected static RegPatientDtoResponse makeDtoResponseFromPatient(Patient patient) {
        RegPatientDtoResponse response = new RegPatientDtoResponse();
        UserService.makeDtoResponseFromUser(response, patient);
        response.setId(patient.getId());
        response.setAddress(patient.getAddress());
        response.setEmail(patient.getEmail());
        response.setPhone(patient.getPhone());
        return response;
    }

    public String registerPatient(String registerPatientJson) {
        RegPatientDtoRequest regPatientDtoRequest = gson.fromJson(registerPatientJson, RegPatientDtoRequest.class);
        Patient patient = makePatientFromDtoRequest(regPatientDtoRequest);
        patientDao.insertPatient(patient);
        RegPatientDtoResponse response = makeDtoResponseFromPatient(patient);
        return gson.toJson(response);
    }

    public String updatePatient(String updatePatientJson) {
        return null;
    }

    public String makeAppointment (String makeAppointmentJson, String token) {
        MakeAppointmentDtoRequest request = gson.fromJson(makeAppointmentJson, MakeAppointmentDtoRequest.class);
        int userId = userDao.getUserIdByToken(token);
        Patient patient = patientDao.getPatientByUserId(userId);
        int doctorId = request.getDoctorId();
        Doctor doctor = doctorDao.getDoctorWithoutScheduleByDoctorId(doctorId);
        String ticketNumber =  "D" + doctorId + request.getDate().replace("-","") + request.getTime().replace(":", "");
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(request.getDate(), formatterDate);
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime timeStart = LocalTime.parse(request.getTime(), formatterTime);
        int slotId = patientDao.getSlotIdByDateTime(doctorId, date, timeStart);
        patientDao.makeAppointment(patient, slotId, ticketNumber);
        MakeAppointmentDtoResponse response = new MakeAppointmentDtoResponse();
        response.setTicket(ticketNumber);
        response.setDoctorId(doctorId);
        response.setFirstName(doctor.getFirstName());
        response.setLastName(doctor.getLastName());
        response.setPatronymic(doctor.getPatronymic());
        response.setSpeciality(doctor.getSpeciality());
        response.setRoom(doctor.getRoom());
        response.setDate(request.getDate());
        response.setTime(request.getTime());

        return gson.toJson(response);
    }


}
