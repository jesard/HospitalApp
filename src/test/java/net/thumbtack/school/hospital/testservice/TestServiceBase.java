package net.thumbtack.school.hospital.testservice;

import net.thumbtack.school.hospital.TestBase;
import net.thumbtack.school.hospital.dto.request.*;
import net.thumbtack.school.hospital.dto.request.regdoctor.RegDocDtoRequest;
import net.thumbtack.school.hospital.dto.request.regdoctor.WeekSchedule;
import net.thumbtack.school.hospital.dto.response.*;
import net.thumbtack.school.hospital.dto.response.regdoctor.RegDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.response.regdoctor.TicketDtoResponse;
import net.thumbtack.school.hospital.error.ServerErrorCode;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.service.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestServiceBase extends TestBase {

    protected AdminService adminService = new AdminService();
    protected DoctorService doctorService = new DoctorService();
    protected PatientService patientService = new PatientService();
    protected UserService userService = new UserService();
    protected ScheduledService scheduledService = new ScheduledService();

    protected RegDoctorDtoResponse insertDoctor1(String token) throws ServerException {

        RegDocDtoRequest regDocRequest = new RegDocDtoRequest("Геннадий", "Ветров", "Васильевич", "mekerrer", "wefmwefwlekf", "surgeon", "555a", "05-06-2020", "05-07-2020", 15);
        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        weekDays.add("Tue");
        WeekSchedule weekSchedule = new WeekSchedule("09:00", "15:00", weekDays);
        regDocRequest.setWeekSchedule(weekSchedule);

        return doctorService.registerDoctor(regDocRequest, token);
    }

    protected RegDoctorDtoResponse insertDoctor2(String token) throws ServerException {
        RegDocDtoRequest regDocRequest = new RegDocDtoRequest("Марат", "Веретенников", "Васильевич", "eregergwww", "vdfvffvdfv", "surgeon", "302a", "15-06-2020", "18-07-2020", 15);
        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        WeekSchedule weekSchedule = new WeekSchedule("14:00", "17:00", weekDays);
        regDocRequest.setWeekSchedule(weekSchedule);

        return doctorService.registerDoctor(regDocRequest, token);
    }

    private RegPatientDtoResponse insertPatientByService1() throws ServerException {
        RegPatientDtoRequest regPatientDtoRequest = new RegPatientDtoRequest("Василий","Мураев", "Иванович", "efef@wefwe.ru", "frfrferf", "489348", "faffasf", "fefefefefefef");
        return patientService.registerPatient(regPatientDtoRequest);
    }

    private RegPatientDtoResponse insertPatientByService2() throws ServerException {
        RegPatientDtoRequest regPatientDtoRequest = new RegPatientDtoRequest("Евгений","Карузо", "Петрович", "fmdko@wefwe.ru", "ививаи", "56456456", "kkkkiiky", "rfgergergwq");
        return patientService.registerPatient(regPatientDtoRequest);
    }

    private MakeAppointmentDtoResponse makeAppointment(int doctorId, String date, String time, String patientToken) throws ServerException {
        MakeAppointmentDtoRequest makeAppointmentDtoRequest = new MakeAppointmentDtoRequest(doctorId, "", date,time);
        return patientService.makeAppointment(makeAppointmentDtoRequest, patientToken);
    }

    @Test
    public void testAdminRegister() throws ServerException {
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("admin", "qwerty");
        String adminToken = userService.login(loginDtoRequest);

        RegAdminDtoRequest regAdminDtoRequest = new RegAdminDtoRequest("имяАдмин", "фамилия", "отчество", "должность", "aaaaa", "qwerty");

        RegAdminDtoResponse response = adminService.registerAdmin(regAdminDtoRequest, adminToken);
        assertEquals(response.getFirstName(), "имяАдмин");
    }

    @Test
    public void testPatientRegister() throws ServerException {
        RegPatientDtoResponse response = insertPatientByService1();
        assertEquals(response.getFirstName(), "Василий");
    }

    @Test
    public void testLoginCaseSensitive() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        assertNotNull(tokenAdmin);

        LoginDtoRequest loginAdmin2= new LoginDtoRequest("AdMin", "qwerty");
        String tokenAdmin2 = userService.login(loginAdmin2);
        assertNotNull(tokenAdmin2);

        LoginDtoRequest loginAdmin3= new LoginDtoRequest("admin", "Qwerty");
        assertThrows(ServerException.class, () -> userService.login(loginAdmin3));

        LoginDtoRequest loginAdmin4= new LoginDtoRequest("Admin", "Qwerty");
        assertThrows(ServerException.class, () -> userService.login(loginAdmin4));
    }

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
    public void testMakeAppointmentMoreThanTwoMonthAhead() throws ServerException {
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
    public void testMakeAppointmentInvalidDateTime() throws ServerException {
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
        assertEquals("Мураев", response.getSchedule().get(0).getDaySchedule().get(0).getPatient().getLastName());
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
    public void testDeleteDoctor() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse response1 = insertDoctor1(tokenAdmin);
        DeleteDoctorDtoRequest deleteDoctorDtoRequest = new DeleteDoctorDtoRequest("10-05-2020");
        doctorService.deleteDoctor(deleteDoctorDtoRequest, response1.getId(), tokenAdmin);
        RegDoctorDtoResponse fromDb = doctorService.getDoctor(response1.getId(), "yes", null, null, tokenAdmin);
        assertEquals(0, fromDb.getSchedule().size());
        assertEquals(1, scheduledService.deleteDoctorsWithTerminationDate());
    }

    @Test
    public void testBusyRoom() throws ServerException {
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
        assertEquals(response1.getRoom(), response2.getRoom());
    }

    @Test
    public void testSameDocSameDayAppointment() throws ServerException {
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
    public void testBusyPatient() throws ServerException {
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



}

