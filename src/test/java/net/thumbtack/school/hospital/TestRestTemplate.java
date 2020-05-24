package net.thumbtack.school.hospital;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.request.MakeAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegCommissionDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegPatientDtoRequest;
import net.thumbtack.school.hospital.dto.request.regdoctor.RegDocDtoRequest;
import net.thumbtack.school.hospital.dto.request.regdoctor.WeekSchedule;
import net.thumbtack.school.hospital.dto.response.RegPatientDtoResponse;
import net.thumbtack.school.hospital.dto.response.regdoctor.RegDoctorDtoResponse;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestRestTemplate extends TestBase {

    private static RestTemplate template = new RestTemplate();

    @Autowired
    private ObjectMapper mapper;

    private final String BASEURL = "http://localhost:8080/api";

    @BeforeAll()
    public static void addConverter() {
        template.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    private String login(String login, String password) throws JsonProcessingException {
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest(login, password);
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> loginRequest = new HttpEntity<>(mapper.writeValueAsString(loginDtoRequest), headers1);
        HttpEntity<String> loginResponse = template.exchange(BASEURL + "/sessions", HttpMethod.POST, loginRequest, String.class);
        HttpHeaders headers = loginResponse.getHeaders();
        String set_cookie = headers.getFirst(HttpHeaders.SET_COOKIE);
        return set_cookie != null ? set_cookie.replace("JAVASESSIONID=", "") : null;
    }

    private String loginSuperAdmin() throws JsonProcessingException {
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("admin", "qwerty");
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> loginRequest = new HttpEntity<>(mapper.writeValueAsString(loginDtoRequest), headers1);
        HttpEntity<String> loginResponse = template.exchange(BASEURL + "/sessions", HttpMethod.POST, loginRequest, String.class);
        HttpHeaders headers = loginResponse.getHeaders();
        String set_cookie = headers.getFirst(HttpHeaders.SET_COOKIE);
        return set_cookie != null ? set_cookie.replace("JAVASESSIONID=", "") : null;
    }

    private RegDoctorDtoResponse regDoctor(Doctor doctor) throws JsonProcessingException {
        String token = loginSuperAdmin();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "JAVASESSIONID=" + token);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        RegDocDtoRequest dto = new RegDocDtoRequest(doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getLogin(), doctor.getPassword(), doctor.getSpeciality(), doctor.getRoom(), "05-07-2020", "05-08-2020", 20);
        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        WeekSchedule weekSchedule = new WeekSchedule("14:00", "17:00", weekDays);
        dto.setWeekSchedule(weekSchedule);
        HttpEntity<String> regDocRequest = new HttpEntity<>(mapper.writeValueAsString(dto), requestHeaders);
        ResponseEntity<RegDoctorDtoResponse> response2 = template.exchange(BASEURL + "/doctors", HttpMethod.POST, regDocRequest, RegDoctorDtoResponse.class);
        return response2.getBody();
    }

    private RegPatientDtoResponse regPatient(Patient patient) throws JsonProcessingException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        RegPatientDtoRequest regPatientDto = new RegPatientDtoRequest(patient.getFirstName(),patient.getLastName(), patient.getPatronymic(), patient.getEmail(), patient.getAddress(), patient.getPhone(), patient.getLogin(), patient.getPassword());
        HttpEntity<String> regPatientRequest = new HttpEntity<>(mapper.writeValueAsString(regPatientDto), requestHeaders);
        ResponseEntity<RegPatientDtoResponse> response = template.exchange(BASEURL + "/patients", HttpMethod.POST, regPatientRequest, RegPatientDtoResponse.class);
        return response.getBody();
    }

    private String makeAppointment(int docId1, String speciality, String date, String time, String token) throws JsonProcessingException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "JAVASESSIONID=" + token);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        MakeAppointmentDtoRequest dto = new MakeAppointmentDtoRequest(docId1, speciality, date, time);
        HttpEntity<String> makeAppReq = new HttpEntity<>(mapper.writeValueAsString(dto), requestHeaders);
        ResponseEntity<String> response = template.exchange(BASEURL + "/tickets", HttpMethod.POST, makeAppReq, String.class);
        return response.getBody();
    }

    private String regCommission(int patientId1, List<Integer> docIds, String room, String date, String time, int duration, String token) throws JsonProcessingException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "JAVASESSIONID=" + token);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        RegCommissionDtoRequest regCommissionDto = new RegCommissionDtoRequest(patientId1, docIds, room, date, time, duration);
        HttpEntity<String> regCommissionReq = new HttpEntity<>(mapper.writeValueAsString(regCommissionDto), requestHeaders);
        return template.exchange(BASEURL + "/commissions", HttpMethod.POST, regCommissionReq, String.class).getBody();
    }


    @Test
    public void testGetAccountInfoAdmin() throws JsonProcessingException {
        String token = loginSuperAdmin();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "JAVASESSIONID=" + token);
        HttpEntity<String> requestAdmin = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<Admin> response2 = template.exchange(BASEURL + "/account", HttpMethod.GET, requestAdmin, Admin.class);
        Admin admin = response2.getBody();

        if (admin != null) {
            assertEquals("superadmin", admin.getPosition());
        } else fail();
    }

    @Test
    public void testRegisterDoctor() throws JsonProcessingException {
        Doctor doctor = new Doctor("Марат", "Веретенников", "Васильевич", "eregergwww", "vdfvffvdfv", "surgeon", "302a");
        RegDoctorDtoResponse response = regDoctor(doctor);
        Assertions.assertNotNull(response);
        assertEquals("Веретенников", response.getLastName());
    }

    @Test
    public void testDoctorBusyByAppointment() throws JsonProcessingException {
        Doctor doctor1 = new Doctor("Марат", "Веретенников", "Васильевич", "eregergwww", "vdfvffvdfv", "surgeon", "302a");
        Doctor doctor2 = new Doctor("Вениамин", "Жораев", "Никодимович", "eregwww", "vsdvsvvvff", "therapist", "111");
        Patient patient1 = new Patient("Василий","Мураев", "Иванович", "fvdfvde", "frfrfersf", "efdef@wefwe.ru", "faffasf", "+79845447788");
        Patient patient2 = new Patient("Иван","Лапшин", "Михайлович", "vevrev", "eerervervre", "erf@wefwe.ru", "eerferf", "+79912343322");
        RegPatientDtoResponse responsePatient1 = regPatient(patient1);
        RegPatientDtoResponse responsePatient2 = regPatient(patient2);
        RegDoctorDtoResponse responseDoc1 = regDoctor(doctor1);
        RegDoctorDtoResponse responseDoc2 = regDoctor(doctor2);

        int docId1 = responseDoc1.getId();
        int docId2 = responseDoc2.getId();
        int patientId1 = responsePatient1.getId();
        int patientId2 = responsePatient2.getId();

        String patientToken1 = login(patient1.getLogin(), patient1.getPassword());
        String patientToken2 = login(patient2.getLogin(), patient2.getPassword());
        String doctorToken1 = login(doctor1.getLogin(), doctor1.getPassword());
        String doctorToken2 = login(doctor2.getLogin(), doctor2.getPassword());

        String appDate = responseDoc1.getSchedule().get(0).getDate();
        String appTime = responseDoc1.getSchedule().get(0).getDaySchedule().get(0).getTime();
        String makeAppResponse = makeAppointment(docId1, "", appDate, appTime, patientToken1);
        assertNotNull(makeAppResponse);

        //doctor busy by another patient for appoint
        try {
            makeAppointment(docId1, "", appDate, appTime, patientToken2);
            fail();
        }
        catch (HttpStatusCodeException ex) {
            String responseString = ex.getResponseBodyAsString();
            assertTrue(responseString.contains("SLOT_IS_BUSY"));
        }

        //doctor busy by another patient for commission
        List<Integer> docIds = Arrays.asList(docId1, docId2);
        try {
            regCommission(patientId2, docIds, doctor1.getRoom(), appDate, appTime, 30, doctorToken1);
            fail();
        } catch (HttpStatusCodeException ex) {
            String responseString = ex.getResponseBodyAsString();
            assertTrue(responseString.contains("DOCTOR_IS_BUSY"));
        }
    }

    @Test
    public void testDoctorBusyByCommission() throws JsonProcessingException {
        Doctor doctor1 = new Doctor("Марат", "Веретенников", "Васильевич", "eregergwww", "vdfvffvdfv", "surgeon", "302a");
        Doctor doctor2 = new Doctor("Вениамин", "Жораев", "Никодимович", "eregwww", "vsdvsvvvff", "therapist", "111");
        Patient patient1 = new Patient("Василий","Мураев", "Иванович", "fvdfvde", "frfrfersf", "efdef@wefwe.ru", "faffasf", "+79845447788");
        Patient patient2 = new Patient("Иван","Лапшин", "Михайлович", "vevrev", "eerervervre", "erf@wefwe.ru", "eerferf", "+79912343322");
        RegPatientDtoResponse responsePatient1 = regPatient(patient1);
        RegPatientDtoResponse responsePatient2 = regPatient(patient2);
        RegDoctorDtoResponse responseDoc1 = regDoctor(doctor1);
        RegDoctorDtoResponse responseDoc2 = regDoctor(doctor2);

        int docId1 = responseDoc1.getId();
        int docId2 = responseDoc2.getId();
        int patientId1 = responsePatient1.getId();
        int patientId2 = responsePatient2.getId();

        String patientToken1 = login(patient1.getLogin(), patient1.getPassword());
        String patientToken2 = login(patient2.getLogin(), patient2.getPassword());
        String doctorToken1 = login(doctor1.getLogin(), doctor1.getPassword());
        String doctorToken2 = login(doctor2.getLogin(), doctor2.getPassword());

        String date = responseDoc1.getSchedule().get(0).getDate();
        String time = responseDoc1.getSchedule().get(0).getDaySchedule().get(0).getTime();

        List<Integer> docIds = Collections.singletonList(docId2);
        String regCommissionResponse = regCommission(patientId1, docIds, doctor1.getRoom(), date, time, 40, doctorToken1);
        assertTrue(regCommissionResponse.contains("\"patientId\":" + patientId1));
        assertTrue(regCommissionResponse.contains(String.valueOf(docId1)));

        try {
            makeAppointment(0, "surgeon", date, time, patientToken2);
            fail();
        }
        catch (HttpStatusCodeException ex) {
            String responseString = ex.getResponseBodyAsString();
            assertTrue(responseString.contains("SLOT_IS_BUSY"));
        }

    }

    @Test
    public void testComplexScenario() {}


}
