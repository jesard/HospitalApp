package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegPatientDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.RegPatientDtoResponse;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class PatientsEndpoint {

    PatientService patientService = new PatientService();

    @PostMapping(value = "/patients", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegPatientDtoResponse> insertPatient(@Valid @RequestBody RegPatientDtoRequest request, HttpServletResponse response) throws ServerException {
        RegPatientDtoResponse regPatientDtoResponse = patientService.registerPatient(request);
        String token = patientService.login(new LoginDtoRequest(request.getLogin(), request.getPassword()));
        Cookie cookie = new Cookie("JAVASESSIONID", token);
        response.addCookie(cookie);
        return new ResponseEntity<>(regPatientDtoResponse, HttpStatus.OK);

    }

    @GetMapping(value = "/patients/{patientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RegPatientDtoResponse getPatientInfo(@PathVariable("patientId") int patientId, @CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) throws ServerException {
        return patientService.getPatientInfoByPatientId(patientId, token);
    }

    //PUT /api/patients
    @PutMapping(value = "/patients", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RegPatientDtoResponse updatePatient (@Valid @RequestBody UpdatePatientDtoRequest request, @CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) throws ServerException {
        return patientService.updatePatient(request, token);

    }


}
