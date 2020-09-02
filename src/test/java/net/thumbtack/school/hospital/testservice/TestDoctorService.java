package net.thumbtack.school.hospital.testservice;

import net.thumbtack.school.hospital.dto.request.DeleteDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.request.MakeAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegCommissionDtoRequest;
import net.thumbtack.school.hospital.dto.request.regdoctor.RegDocDtoRequest;
import net.thumbtack.school.hospital.dto.request.regdoctor.WeekSchedule;
import net.thumbtack.school.hospital.dto.response.*;
import net.thumbtack.school.hospital.dto.response.regdoctor.DayScheduleDtoResponse;
import net.thumbtack.school.hospital.dto.response.regdoctor.RegDoctorDtoResponse;
import net.thumbtack.school.hospital.error.ServerErrorCode;
import net.thumbtack.school.hospital.error.ServerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class TestDoctorService extends TestServiceBase {


    @Test
    public void testDoctorRegisterWithWrongToken() {
        String token = "wrongToken";
        assertThrows(ServerException.class, ()-> insertDoctor2(token));
    }

    @Test
    public void testGetDoctorByToken() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse response1 = insertDoctor1(tokenAdmin);
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("mekerrer", "wefmwefwlekf");
        String token1 = userService.login(loginDtoRequest);
        RegUserDtoResponse response = userService.getUserDtoResponseByToken(token1);
        assertTrue(response.getLastName().contains("Ветров"));
    }

    @Test
    public void testGetDoctor() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse response1 = insertDoctor1(tokenAdmin);
        RegPatientDtoResponse response3 = insertPatientByService1();
        RegPatientDtoResponse response4 = insertPatientByService2();

        int docId1 = response1.getId();

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("faffasf", "fefefefefefef");
        String tokenPatient1 = userService.login(loginDtoRequest);
        LoginDtoRequest loginDtoRequest2= new LoginDtoRequest("kkkkiiky", "rfgergergwq");
        String tokenPatient2 = userService.login(loginDtoRequest2);


        MakeAppointmentDtoRequest makeAppointmentDtoRequest = new MakeAppointmentDtoRequest(docId1, "", "08-06-2020","09:00");
        patientService.makeAppointment(makeAppointmentDtoRequest, tokenPatient1);

        RegDoctorDtoResponse response = doctorService.getDoctor(docId1, "yes", null, null, tokenPatient1);
        assertEquals(docId1, response.getId());
        assertEquals(response1.getLastName(), response.getLastName());
    }

    @Test
    public void testGetDoctors() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse responseDoc1 = insertDoctor1(tokenAdmin);
        RegDoctorDtoResponse responseDoc2 = insertDoctor2(tokenAdmin);
        RegPatientDtoResponse response3 = insertPatientByService1();
        RegPatientDtoResponse response4 = insertPatientByService2();
        int docId1 = responseDoc1.getId();
        int docId2 = responseDoc2.getId();
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("faffasf", "fefefefefefef");
        String tokenPatient1 = userService.login(loginDtoRequest);
        LoginDtoRequest loginDtoRequest2= new LoginDtoRequest("kkkkiiky", "rfgergergwq");
        String tokenPatient2 = userService.login(loginDtoRequest2);
        MakeAppointmentDtoRequest makeAppointmentDtoRequest = new MakeAppointmentDtoRequest(docId1, "", "08-06-2020","09:00");
        patientService.makeAppointment(makeAppointmentDtoRequest, tokenPatient1);
        MakeAppointmentDtoRequest makeAppointmentDtoRequest2 = new MakeAppointmentDtoRequest(docId2, "", "15-06-2020","16:45");
        patientService.makeAppointment(makeAppointmentDtoRequest2, tokenPatient2);

        List<RegDoctorDtoResponse> doctors = doctorService.getDoctors("yes", null, "08-06-2020", "15-06-2020", tokenAdmin);
        assertEquals(2, doctors.size());
        List<DayScheduleDtoResponse> schedule1 = doctors.get(0).getSchedule();
        List<DayScheduleDtoResponse> schedule2 = doctors.get(1).getSchedule();
        assertEquals("Мураев", schedule1.get(0).getDaySchedule().get(0).getPatient().getLastName());
        assertEquals("Карузо", schedule2.get(0).getDaySchedule().get(schedule2.get(0).getDaySchedule().size() - 1).getPatient().getLastName());


    }

    @Test
    public void testUpdateSchedule() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse response1 = insertDoctor1(tokenAdmin);
        int docId = response1.getId();
        List<String> weekDays = new ArrayList<>();
        weekDays.add("Tue");
        WeekSchedule weekSchedule = new WeekSchedule("15:00", "16:00", weekDays);
        RegDocDtoRequest request = new RegDocDtoRequest("15-06-2020", "05-07-2020", weekSchedule, 20);
        RegDoctorDtoResponse response = doctorService.updateDoctorSchedule(request, docId, tokenAdmin);
        assertEquals("09:00", response.getSchedule().get(0).getDaySchedule().get(0).getTime());
        assertEquals("15:00", response.getSchedule().get(response.getSchedule().size() - 1).getDaySchedule().get(0).getTime());
    }

    @Test
    public void testDeleteDoctor() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse response1 = insertDoctor1(tokenAdmin);
        DeleteDoctorDtoRequest deleteDoctorDtoRequest = new DeleteDoctorDtoRequest("10-05-2020");
        doctorService.deleteDoctor(deleteDoctorDtoRequest, response1.getId(), tokenAdmin);
        RegDoctorDtoResponse fromDb = doctorService.getDoctor(response1.getId(), "yes", null, null, tokenAdmin);
        Assertions.assertEquals(0, fromDb.getSchedule().size());
        Assertions.assertEquals(1, scheduledService.deleteDoctorsWithTerminationDate());
    }

    @Test
    public void testScheduleBusyRoom() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        insertDoctor1(tokenAdmin);

        RegDocDtoRequest regDocRequest = new RegDocDtoRequest("Доктор", "Второй", "Васильевич", "vvsdvsv", "wewewewe", "surgeon", "555a", "29-06-2020", "05-07-2020", 15);
        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        WeekSchedule weekSchedule = new WeekSchedule("14:59", "16:00", weekDays);
        regDocRequest.setWeekSchedule(weekSchedule);
        try {
            doctorService.registerDoctor(regDocRequest, tokenAdmin);
            fail();
        } catch (ServerException ex) {
            assertSame(ex.getErrors().get(0).getErrorCode(), ServerErrorCode.ROOM_IS_BUSY);
        }

    }

    @Test
    public void testScheduleSameRoomDifferentTime() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse response1 = insertDoctor1(tokenAdmin);

        RegDocDtoRequest regDocRequest = new RegDocDtoRequest("Доктор", "Второй", "Васильевич", "vvsdvsv", "wewewewe", "surgeon", "555a", "29-06-2020", "05-07-2020", 15);
        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        WeekSchedule weekSchedule = new WeekSchedule("15:00", "16:00", weekDays);
        regDocRequest.setWeekSchedule(weekSchedule);
        RegDoctorDtoResponse response2 = doctorService.registerDoctor(regDocRequest, tokenAdmin);
        Assertions.assertEquals(response1.getRoom(), response2.getRoom());
    }

    @Test
    public void testRegisterCommission() throws ServerException {
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
        assertEquals(2, response.getDoctorIds().size());
        assertEquals(date, response.getDate());
        assertEquals(time, response.getTime());
        assertEquals(patientId1, response.getPatientId());
        GetTicketsDtoResponse ticketsDtoResponse = patientService.getTickets(tokenPatient1);
        assertEquals(1, ticketsDtoResponse.getTickets().size());
        assertEquals(2, ticketsDtoResponse.getTickets().get(0).getDoctors().size());
        assertEquals(date, ticketsDtoResponse.getTickets().get(0).getDate());
    }

    @Test
    public void testRegisterCommissionSaturday() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse responseDoc1 = insertDoctor1(tokenAdmin);
        RegDoctorDtoResponse responseDoc2 = insertDoctor2(tokenAdmin);
        RegPatientDtoResponse responsePatient1 = insertPatientByService1();
        int docId1 = responseDoc1.getId();
        int docId2 = responseDoc2.getId();
        int patientId1 = responsePatient1.getId();
        LoginDtoRequest loginDoctor1 = new LoginDtoRequest("mekerrer", "wefmwefwlekf");
        String doctorToken1 = userService.login(loginDoctor1);

        String date = "13-06-2020";
        String time = responseDoc1.getSchedule().get(0).getDaySchedule().get(0).getTime();

        List<Integer> docIds = Arrays.asList(docId1, docId2);
        RegCommissionDtoRequest request = new RegCommissionDtoRequest(patientId1, docIds, responseDoc1.getRoom(), date, time, 40);
        try {
            doctorService.registerCommission(request, doctorToken1);
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.WRONG_DATE, ex.getErrors().get(0).getErrorCode());
        }
    }



}
