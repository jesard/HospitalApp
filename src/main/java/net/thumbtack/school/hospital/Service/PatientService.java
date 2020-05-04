package net.thumbtack.school.hospital.Service;

import com.google.gson.Gson;
import net.thumbtack.school.hospital.dao.PatientDao;
import net.thumbtack.school.hospital.daoimpl.PatientDaoImpl;
import net.thumbtack.school.hospital.dto.request.RegPatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.RegPatientDtoResponse;
import net.thumbtack.school.hospital.model.Patient;

public class PatientService {

    private PatientDao patientDao = new PatientDaoImpl();
    private Gson gson = new Gson();

    private Patient makePatientFromDtoRequest(RegPatientDtoRequest request) {
        Patient patient = new Patient();
        UserService.makeUserFromDtoRequest(patient, request);
        patient.setAddress(request.getAddress());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        return patient;
    }

    private RegPatientDtoResponse makeDtoResponseFromPatient(Patient patient) {
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


}
