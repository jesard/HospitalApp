package net.thumbtack.school.hospital.testservice;

import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegCommissionDtoRequest;
import net.thumbtack.school.hospital.dto.response.GetTicketsDtoResponse;
import net.thumbtack.school.hospital.dto.response.MakeAppointmentDtoResponse;
import net.thumbtack.school.hospital.dto.response.RegCommissionDtoResponse;
import net.thumbtack.school.hospital.dto.response.RegPatientDtoResponse;
import net.thumbtack.school.hospital.dto.response.regdoctor.RegDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.response.regdoctor.TicketDtoResponse;
import net.thumbtack.school.hospital.error.ServerErrorCode;
import net.thumbtack.school.hospital.error.ServerException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;

public class TestPatientService extends TestServiceBase {

    @Test
    public void testMakeAppointment() throws ServerException {
        insertPatientByService1();
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("faffasf", "fefefefefefef");
        String patientToken = userService.login(loginDtoRequest);
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse regDoc = insertDoctor1(tokenAdmin);
        MakeAppointmentDtoResponse response2 = makeAppointment(regDoc.getId(), "08-06-2020", "09:00", patientToken);

        assertTrue(response2.getTicket().contains("08062020"));

        GetTicketsDtoResponse response3 = patientService.getTickets(patientToken);
        boolean ticketInDb = false;
        for (TicketDtoResponse ticket: response3.getTickets()) {
            if (ticket.getTicket().contains("08062020")) {
                ticketInDb = true;
                break;
            }
        }
        if (!ticketInDb) fail();
    }

    @Test
    public void testAppointmentWithSpeciality() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse responseDoc1 = insertDoctor1(tokenAdmin);
        RegPatientDtoResponse response2 = insertPatientByService1();
        int docId1 = responseDoc1.getId();
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("faffasf", "fefefefefefef");
        String tokenPatient = userService.login(loginDtoRequest);
        MakeAppointmentDtoResponse responseAppointment1 = makeAppointment("surgeon", "08-06-2020", "09:00", tokenPatient);
        assertEquals("08-06-2020", responseAppointment1.getDate());
        try {
            makeAppointment("plumber", "08-06-2020", "09:15", tokenPatient);
        } catch (ServerException ex) {
            assertEquals(ex.getErrors().get(0).getErrorCode(), ServerErrorCode.SPECIALITY_NOT_FOUND);
        }

    }

    @Test
    public void testAppointmentMoreThanTwoMonthAhead() throws ServerException {
        RegPatientDtoResponse response1 = insertPatientByService1();
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("faffasf", "fefefefefefef");
        String patientToken = userService.login(loginDtoRequest);
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse regDoc = insertDoctor1(tokenAdmin);
        try {
            makeAppointment(regDoc.getId(), "08-06-2021", "09:00", patientToken);
            fail();
        } catch (ServerException ex) {
            assertSame(ex.getErrors().get(0).getErrorCode(), ServerErrorCode.WRONG_DATE);
        }
    }

    @Test
    public void testAppointmentInvalidDateTime() throws ServerException {
        RegPatientDtoResponse response1 = insertPatientByService1();
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("faffasf", "fefefefefefef");
        String patientToken = userService.login(loginDtoRequest);
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse regDoc = insertDoctor1(tokenAdmin);
        try {
            makeAppointment(regDoc.getId(), "07-06-2020", "09:00", patientToken);
            fail();
        } catch (ServerException ex) {
            assertSame(ex.getErrors().get(0).getErrorCode(), ServerErrorCode.SCHEDULE_NOT_EXISTS);
        }
    }

    @Test
    public void testAppointmentSameDocSameDay() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse regDoc = insertDoctor1(tokenAdmin);
        insertPatientByService1();

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("faffasf", "fefefefefefef");
        String patientToken = userService.login(loginDtoRequest);
        makeAppointment(regDoc.getId(), "08-06-2020","09:00", patientToken);
        try {
            makeAppointment(regDoc.getId(),"08-06-2020","09:15", patientToken);
            fail();
        } catch (ServerException ex) {
            assertSame(ex.getErrors().get(0).getErrorCode(), ServerErrorCode.SAME_DAY_SAME_DOCTOR);
        }
    }

    @Test
    public void testAppointmentBusyPatient() throws ServerException {
        insertPatientByService1();
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("faffasf", "fefefefefefef");
        String patientToken = userService.login(loginDtoRequest);
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);

        RegDoctorDtoResponse regDoc = insertDoctor1(tokenAdmin);
        RegDoctorDtoResponse regDoc2 = insertDoctor2(tokenAdmin);

        makeAppointment(regDoc.getId(), "15-06-2020", "14:45", patientToken);
        try {
            makeAppointment(regDoc2.getId(), "15-06-2020", "14:45", patientToken);
            fail();
        } catch (ServerException ex) {
            assertSame(ex.getErrors().get(0).getErrorCode(), ServerErrorCode.PATIENT_IS_BUSY);
        }


    }

    @Test
    public void testGetPatientByToken() throws ServerException {
        insertPatientByService1();

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("faffasf", "fefefefefefef");

        String token1 = userService.login(loginDtoRequest);
        RegPatientDtoResponse response = (RegPatientDtoResponse) userService.getUserDtoResponseByToken(token1);

//        assertTrue(response.g.contains("адрес"));
//        assertTrue(response.contains("номер"));
    }

    @Test
    public void testGetTickets() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse responseDoc1 = insertDoctor1(tokenAdmin);
        RegDoctorDtoResponse responseDoc2 = insertDoctor2(tokenAdmin);
        RegPatientDtoResponse responsePatient1 = insertPatientByService1();
        int docId1 = responseDoc1.getId();
        int docId2 = responseDoc2.getId();
        int patientId1 = responsePatient1.getId();
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("faffasf", "fefefefefefef");
        String tokenPatient1 = userService.login(loginDtoRequest);
        LoginDtoRequest loginDoctor1 = new LoginDtoRequest("mekerrer", "wefmwefwlekf");
        String doctorToken1 = userService.login(loginDoctor1);

        String dateApp = responseDoc1.getSchedule().get(0).getDate();
        String timeApp = responseDoc1.getSchedule().get(0).getDaySchedule().get(0).getTime();
        MakeAppointmentDtoResponse responseApp = makeAppointment(docId1, dateApp, timeApp, tokenPatient1);

        String dateCommission = responseDoc1.getSchedule().get(1).getDate();
        String timeCommission = responseDoc1.getSchedule().get(1).getDaySchedule().get(0).getTime();
        List<Integer> docIds = Arrays.asList(docId1, docId2);
        RegCommissionDtoRequest request = new RegCommissionDtoRequest(patientId1, docIds, responseDoc2.getRoom(), dateCommission, timeCommission, 40);
        RegCommissionDtoResponse responseComm = doctorService.registerCommission(request, doctorToken1);

        GetTicketsDtoResponse ticketsDtoResponse = patientService.getTickets(tokenPatient1);
        assertEquals(2, ticketsDtoResponse.getTickets().size());
        assertEquals(docId1, ticketsDtoResponse.getTickets().get(0).getDoctor().getId());
        assertEquals(dateApp, ticketsDtoResponse.getTickets().get(0).getDate());
        assertEquals(2, ticketsDtoResponse.getTickets().get(1).getDoctors().size());
        assertEquals(dateCommission, ticketsDtoResponse.getTickets().get(1).getDate());
    }

    @Test
    public void testDeleteCommission() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse responseDoc1 = insertDoctor1(tokenAdmin);
        RegDoctorDtoResponse responseDoc2 = insertDoctor2(tokenAdmin);
        RegPatientDtoResponse responsePatient1 = insertPatientByService1();
        int docId1 = responseDoc1.getId();
        int docId2 = responseDoc2.getId();
        int patientId1 = responsePatient1.getId();
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("faffasf", "fefefefefefef");
        String tokenPatient1 = userService.login(loginDtoRequest);
        LoginDtoRequest loginDoctor1 = new LoginDtoRequest("mekerrer", "wefmwefwlekf");
        String doctorToken1 = userService.login(loginDoctor1);
        String date = responseDoc1.getSchedule().get(0).getDate();
        String time = responseDoc1.getSchedule().get(0).getDaySchedule().get(0).getTime();
        List<Integer> docIds = Arrays.asList(docId1, docId2);
        RegCommissionDtoRequest request = new RegCommissionDtoRequest(patientId1, docIds, responseDoc1.getRoom(), date, time, 40);
        RegCommissionDtoResponse response = doctorService.registerCommission(request , doctorToken1);
        String ticketNumber = response.getTicket();
        patientService.deleteCommission(ticketNumber, tokenPatient1);

        GetTicketsDtoResponse ticketsDtoResponse = patientService.getTickets(tokenPatient1);
        assertEquals(0, ticketsDtoResponse.getTickets().size());

    }


}
