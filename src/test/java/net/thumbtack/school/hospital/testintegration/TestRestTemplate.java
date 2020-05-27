package net.thumbtack.school.hospital.testintegration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.hospital.TestBase;
import net.thumbtack.school.hospital.dto.request.*;
import net.thumbtack.school.hospital.dto.request.regdoctor.RegDocDtoRequest;
import net.thumbtack.school.hospital.dto.request.regdoctor.WeekSchedule;
import net.thumbtack.school.hospital.dto.response.*;
import net.thumbtack.school.hospital.dto.response.regdoctor.RegDoctorDtoResponse;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestRestTemplate extends TestBase {

    protected static RestTemplate template = new RestTemplate();

    @Autowired
    protected ObjectMapper mapper;

    protected final String BASEURL = "http://localhost:8080/api";

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

    protected String loginSuperAdmin() throws JsonProcessingException {
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("admin", "qwerty");
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> loginRequest = new HttpEntity<>(mapper.writeValueAsString(loginDtoRequest), headers1);
        HttpEntity<String> loginResponse = template.exchange(BASEURL + "/sessions", HttpMethod.POST, loginRequest, String.class);
        HttpHeaders headers = loginResponse.getHeaders();
        String set_cookie = headers.getFirst(HttpHeaders.SET_COOKIE);
        return set_cookie != null ? set_cookie.replace("JAVASESSIONID=", "") : null;
    }

    private RegDoctorDtoResponse regDoctor(RegDocDtoRequest doctor) throws JsonProcessingException {
        String token = loginSuperAdmin();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "JAVASESSIONID=" + token);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        WeekSchedule weekSchedule = new WeekSchedule("14:00", "17:00", weekDays);
        doctor.setWeekSchedule(weekSchedule);
        HttpEntity<String> regDocRequest = new HttpEntity<>(mapper.writeValueAsString(doctor), requestHeaders);
        ResponseEntity<RegDoctorDtoResponse> response2 = template.exchange(BASEURL + "/doctors", HttpMethod.POST, regDocRequest, RegDoctorDtoResponse.class);
        return response2.getBody();
    }

    private RegPatientDtoResponse regPatient(RegPatientDtoRequest patient) throws JsonProcessingException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        RegPatientDtoRequest regPatientDto = new RegPatientDtoRequest(patient.getFirstName(),patient.getLastName(), patient.getPatronymic(), patient.getEmail(), patient.getAddress(), patient.getPhone(), patient.getLogin(), patient.getPassword());
        HttpEntity<String> regPatientRequest = new HttpEntity<>(mapper.writeValueAsString(regPatientDto), requestHeaders);
        ResponseEntity<RegPatientDtoResponse> response = template.exchange(BASEURL + "/patients", HttpMethod.POST, regPatientRequest, RegPatientDtoResponse.class);
        return response.getBody();
    }

    private MakeAppointmentDtoResponse makeAppointment(int docId1, String speciality, String date, String time, String token) throws JsonProcessingException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "JAVASESSIONID=" + token);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        MakeAppointmentDtoRequest dto = new MakeAppointmentDtoRequest(docId1, speciality, date, time);
        HttpEntity<String> makeAppReq = new HttpEntity<>(mapper.writeValueAsString(dto), requestHeaders);
        ResponseEntity<MakeAppointmentDtoResponse> response = template.exchange(BASEURL + "/tickets", HttpMethod.POST, makeAppReq, MakeAppointmentDtoResponse.class);
        return response.getBody();
    }

    private RegCommissionDtoResponse regCommission(int patientId1, List<Integer> docIds, String room, String date, String time, int duration, String token) throws JsonProcessingException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "JAVASESSIONID=" + token);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        RegCommissionDtoRequest regCommissionDto = new RegCommissionDtoRequest(patientId1, docIds, room, date, time, duration);
        HttpEntity<String> regCommissionReq = new HttpEntity<>(mapper.writeValueAsString(regCommissionDto), requestHeaders);
        return template.exchange(BASEURL + "/commissions", HttpMethod.POST, regCommissionReq, RegCommissionDtoResponse.class).getBody();
    }


    @Test
    public void testGetAccountInfoAdmin() throws JsonProcessingException {
        String token = loginSuperAdmin();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "JAVASESSIONID=" + token);
        HttpEntity<String> requestAdmin = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<RegAdminDtoResponse> response2 = template.exchange(BASEURL + "/account", HttpMethod.GET, requestAdmin, RegAdminDtoResponse.class);
        RegAdminDtoResponse admin = response2.getBody();

        if (admin != null) {
            assertEquals("superadmin", admin.getPosition());
        } else fail();
    }

    @Test
    public void testRegisterDoctor() throws JsonProcessingException {
        RegDocDtoRequest doctor = new RegDocDtoRequest("Марат", "Веретенников", "Васильевич", "eregergwww", "vdfvffvdfv", "surgeon", "302a","05-07-2020", "05-08-2020", 20);
        RegDoctorDtoResponse response = regDoctor(doctor);
        Assertions.assertNotNull(response);
        assertEquals("Веретенников", response.getLastName());
    }

    @Test
    public void testDoctorBusyByAppointment() throws JsonProcessingException {
        RegDocDtoRequest doctor1 = new RegDocDtoRequest("Марат", "Веретенников", "Васильевич", "eregergwww", "vdfvffvdfv", "surgeon", "302a", "05-07-2020", "05-08-2020", 20);
        RegDocDtoRequest doctor2 = new RegDocDtoRequest("Вениамин", "Жораев", "Никодимович", "eregwww", "vsdvsvvvff", "therapist", "111", "05-07-2020", "05-08-2020", 20);
        RegPatientDtoRequest patient1 = new RegPatientDtoRequest("Василий","Первый", "Иванович", "efdef@wefwe.ru", "frfrfersf", "+79845447788", "faffasf", "fvdfvdesdf");
        RegPatientDtoRequest patient2 = new RegPatientDtoRequest("Иван","Второй", "Михайлович", "erf@wefwe.ru", "eerervervre", "+79912343322", "eerferf", "evkmervlke");
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
        MakeAppointmentDtoResponse makeAppResponse = makeAppointment(docId1, "", appDate, appTime, patientToken1);
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
        RegDocDtoRequest doctor1 = new RegDocDtoRequest("Марат", "Веретенников", "Васильевич", "eregergwww", "vdfvffvdfv", "surgeon", "302a", "05-07-2020", "05-08-2020", 20);
        RegDocDtoRequest doctor2 = new RegDocDtoRequest("Вениамин", "Жораев", "Никодимович", "eregwww", "vsdvsvvvff", "therapist", "111", "05-07-2020", "05-08-2020", 20);
        RegPatientDtoRequest patient1 = new RegPatientDtoRequest("Василий","Первый", "Иванович", "efdef@wefwe.ru", "frfrfersf", "+79845447788", "faffasf", "fvdfvdesdf");
        RegPatientDtoRequest patient2 = new RegPatientDtoRequest("Иван","Второй", "Михайлович", "erf@wefwe.ru", "eerervervre", "+79912343322", "eerferf", "evkmervlke");
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
        RegCommissionDtoResponse regCommissionResponse = regCommission(patientId1, docIds, doctor1.getRoom(), date, time, 40, doctorToken1);
        assertEquals(regCommissionResponse.getPatientId(), patientId1);
        assertTrue(regCommissionResponse.getDoctorIds().contains(docId1));

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
    public void testGetSettings() {
        HttpEntity<String> request = new HttpEntity<>("");
        ResponseEntity<GetSettingsDtoResponse> response = template.exchange(BASEURL + "/settings", HttpMethod.GET, request, GetSettingsDtoResponse.class);
        assertTrue(response.getBody().getMaxNameLength() > 0);
        assertTrue(response.getBody().getMinPasswordLength() > 0);
    }

    @Test
    public void testComplexScenario() throws JsonProcessingException {

        RegDocDtoRequest doctor1 = new RegDocDtoRequest("Марат", "Веретенников", "Васильевич", "eregergwww", "vdfvffvdfv", "surgeon", "302a", "05-07-2020", "05-08-2020", 20);
        RegDocDtoRequest doctor2 = new RegDocDtoRequest("Вениамин", "Жораев", "Никодимович", "eregwww", "vsdvsvvvff", "therapist", "111", "05-07-2020", "05-08-2020", 20);
        RegPatientDtoRequest patient1 = new RegPatientDtoRequest("Василий","Первый", "Иванович", "efdef@wefwe.ru", "frfrfersf", "+79845447788", "faffasf", "fvdfvdesdf");
        RegPatientDtoRequest patient2 = new RegPatientDtoRequest("Иван","Второй", "Михайлович", "erf@wefwe.ru", "eerervervre", "+79912343322", "eerferf", "evkmervlke");
        RegPatientDtoRequest patient3 = new RegPatientDtoRequest("Евгений", "Третий", "", "ferfe@wewe.ww", "fwfwe23fw", "+79911235522", "wefwefwef", "erkfreew");
        RegPatientDtoResponse responsePatient1 = regPatient(patient1);
        RegPatientDtoResponse responsePatient2 = regPatient(patient2);
        RegPatientDtoResponse responsePatient3 = regPatient(patient3);
        RegDoctorDtoResponse responseDoc1 = regDoctor(doctor1);
        RegDoctorDtoResponse responseDoc2 = regDoctor(doctor2);
        int docId1 = responseDoc1.getId();
        int docId2 = responseDoc2.getId();
        int patientId1 = responsePatient1.getId();
        int patientId2 = responsePatient2.getId();
        int patientId3 = responsePatient3.getId();
        String patientToken1 = login(patient1.getLogin(), patient1.getPassword());
        String patientToken2 = login(patient2.getLogin(), patient2.getPassword());
        String patientToken3 = login(patient3.getLogin(), patient3.getPassword());
        String doctorToken1 = login(doctor1.getLogin(), doctor1.getPassword());
        String doctorToken2 = login(doctor2.getLogin(), doctor2.getPassword());

        // appointment patient 1 to Doc 1 slot 1
        String appDate1 = responseDoc1.getSchedule().get(0).getDate();
        String appTime1 = responseDoc1.getSchedule().get(0).getDaySchedule().get(0).getTime();
        MakeAppointmentDtoResponse makeAppResponse1 = makeAppointment(docId1, "", appDate1, appTime1, patientToken1);
        // appointment patient 2 to Doc 2 slot 2
        String appDate2 = responseDoc2.getSchedule().get(1).getDate();
        String appTime2 = responseDoc2.getSchedule().get(1).getDaySchedule().get(1).getTime();
        MakeAppointmentDtoResponse makeAppResponse2 = makeAppointment(docId2, "", appDate2, appTime2, patientToken2);
        // appointment patient 3 to Doc 1 slot 3
        String appDate3 = responseDoc1.getSchedule().get(1).getDate();
        String appTime3 = responseDoc1.getSchedule().get(1).getDaySchedule().get(0).getTime();
        MakeAppointmentDtoResponse makeAppResponse3 = makeAppointment(docId1, "", appDate3, appTime3, patientToken3);
        // cancel appointment slot 1
        String ticketNumber1 = makeAppResponse1.getTicket();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "JAVASESSIONID=" + patientToken1);
        HttpEntity<String> delTicket3Req = new HttpEntity<>(requestHeaders);
        template.exchange(BASEURL + "/tickets/" + ticketNumber1, HttpMethod.DELETE, delTicket3Req, EmptyJsonResponse.class);
        // make commission pat 2 with Docs 1,2 slot 1
        RegCommissionDtoResponse commissionDtoResponse = regCommission(patientId2, Arrays.asList(docId1, docId2), doctor1.getRoom(), appDate1, appTime1, 40, doctorToken1);
        // appointment patient 2 to Doc 2 slot 1 //should fail
        try {
            makeAppointment(docId2, "", appDate1, appTime1, patientToken1);
            fail();
        } catch (HttpStatusCodeException ex) {
            assertTrue(ex.getResponseBodyAsString().contains("SLOT_IS_BUSY"));
        }
        // delete Doc 2
        String tokenAdmin = loginSuperAdmin();
        HttpHeaders headersDelDoc = new HttpHeaders();
        headersDelDoc.setContentType(MediaType.APPLICATION_JSON);
        headersDelDoc.add("Cookie", "JAVASESSIONID=" + tokenAdmin);
        DeleteDoctorDtoRequest deleteDoctorDto = new DeleteDoctorDtoRequest("06-07-2020");
        HttpEntity<String> delDoctor2Req = new HttpEntity<>(mapper.writeValueAsString(deleteDoctorDto), headersDelDoc);
        template.exchange(BASEURL + "/doctors/" + docId2, HttpMethod.DELETE, delDoctor2Req, String.class).getBody();
        // update schedule Doc 1
        List<String> weekDays = new ArrayList<>();
        weekDays.add("Tue");
        String newSlotStart = "17:00";
        String newSlotEnd = "19:00";
        WeekSchedule weekSchedule = new WeekSchedule(newSlotStart, newSlotEnd, weekDays);
        String newScheduleStart = "20-07-2020";
        String newScheduleEnd = "20-08-2020";
        RegDocDtoRequest newScheduleDto = new RegDocDtoRequest(newScheduleStart, newScheduleEnd, weekSchedule, 20);
        HttpHeaders headers3 = new HttpHeaders();
        headers3.setContentType(MediaType.APPLICATION_JSON);
        headers3.add("Cookie", "JAVASESSIONID=" + tokenAdmin);
        HttpEntity<String> newScheduleHttp = new HttpEntity<>(mapper.writeValueAsString(newScheduleDto), headers3);
        RegDoctorDtoResponse newScheduleResponse = template.exchange(BASEURL + "/doctors/" + docId1, HttpMethod.PUT, newScheduleHttp, RegDoctorDtoResponse.class).getBody();
        // appointment patient 3 to Doc 1 slot 1 (old schedule)
        MakeAppointmentDtoResponse makeAppResponse4 = makeAppointment(docId1, "", appDate1, appTime1, patientToken3);
        // appointment patient 2 to Doc 1 slot 3 (old schedule) // should fail
        try {
            makeAppointment(docId1, "", appDate3, appTime3, patientToken2);
            fail();
        } catch (HttpStatusCodeException ex) {
            assertTrue(ex.getResponseBodyAsString().contains("SLOT_IS_BUSY"));
        }
        // appointment patient 1 to Doc 1 slot 4 (new schedule)
        String appDate4newSchedule = newScheduleResponse.getSchedule().get(newScheduleResponse.getSchedule().size() - 5).getDate();
        MakeAppointmentDtoResponse makeAppResponse5 = makeAppointment(docId1, "", appDate4newSchedule, newSlotStart, patientToken1);

        assertNotNull(makeAppResponse5);

        HttpHeaders headersTickets3 = new HttpHeaders();
        headersTickets3.add("Cookie", "JAVASESSIONID=" + patientToken3);
        HttpEntity<String> httpTicketsPatient3 = new HttpEntity<>(headersTickets3);
        GetTicketsDtoResponse ticketsPatient3 = template.exchange(BASEURL + "/tickets", HttpMethod.GET, httpTicketsPatient3, GetTicketsDtoResponse.class).getBody();

        HttpHeaders headersTickets1 = new HttpHeaders();
        headersTickets1.add("Cookie", "JAVASESSIONID=" + patientToken1);
        HttpEntity<String> httpTicketsPatient1 = new HttpEntity<>(headersTickets1);
        GetTicketsDtoResponse ticketsPatient1 = template.exchange(BASEURL + "/tickets", HttpMethod.GET, httpTicketsPatient1, GetTicketsDtoResponse.class).getBody();

        assertEquals(2, ticketsPatient3.getTickets().size());
        assertEquals(1, ticketsPatient1.getTickets().size());
    }


}
