package net.thumbtack.school.hospital;

import net.thumbtack.school.hospital.dto.request.*;
import net.thumbtack.school.hospital.dto.request.regdoctor.RegDocDtoRequest;
import net.thumbtack.school.hospital.dto.request.regdoctor.WeekSchedule;
import net.thumbtack.school.hospital.dto.response.*;
import net.thumbtack.school.hospital.dto.response.regdoctor.RegDoctorDtoResponse;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.service.AdminService;
import net.thumbtack.school.hospital.service.DoctorService;
import net.thumbtack.school.hospital.service.PatientService;
import net.thumbtack.school.hospital.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestService extends TestBase {

    private AdminService adminService = new AdminService();
    private DoctorService doctorService = new DoctorService();
    private PatientService patientService = new PatientService();
    private UserService userService = new UserService();


    //TODO Mock Dao

    private RegDoctorDtoResponse insertDoctor1(String token) throws ServerException {

        RegDocDtoRequest regDocRequest = new RegDocDtoRequest("Геннадий", "Ветров", "Васильевич", "mekerrer", "wefmwefwlekf", "surgeon", "555a", "05-06-2020", "05-07-2020", 15);
        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        weekDays.add("Tue");
        WeekSchedule weekSchedule = new WeekSchedule("09:00", "15:00", weekDays);
        regDocRequest.setWeekSchedule(weekSchedule);

        return doctorService.registerDoctor(regDocRequest, token);
    }

    private RegDoctorDtoResponse insertDoctor2(String token) throws ServerException {
        RegDocDtoRequest regDocRequest = new RegDocDtoRequest("Марат", "Веретенников", "Васильевич", "eregergwww", "vdfvffvdfv", "surgeon", "302a", "05-07-2020", "05-08-2020", 15);
        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        WeekSchedule weekSchedule = new WeekSchedule("14:00", "17:00", weekDays);
        regDocRequest.setWeekSchedule(weekSchedule);

        return doctorService.registerDoctor(regDocRequest, token);
    }

    private RegPatientDtoResponse insertPatientByService1() {
        RegPatientDtoRequest regPatientDtoRequest = new RegPatientDtoRequest("Василий","Мураев", "Иванович", "efef@wefwe.ru", "frfrferf", "489348", "faffasf", "fefefefefefef");
        return patientService.registerPatient(regPatientDtoRequest);
    }

    private RegPatientDtoResponse insertPatientByService2() {
        RegPatientDtoRequest regPatientDtoRequest = new RegPatientDtoRequest("Евгений","Карузо", "Петрович", "fmdko@wefwe.ru", "ививаи", "56456456", "kkkkiiky", "rfgergergwq");
        return patientService.registerPatient(regPatientDtoRequest);
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
    public void testDoctorsRegister() throws ServerException {

        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);

        RegDoctorDtoResponse response = insertDoctor2(tokenAdmin);
        assertEquals(response.getFirstName(), "Марат");
    }

    @Test
    public void testPatientRegister() {
        RegPatientDtoResponse response = insertPatientByService1();
        assertEquals(response.getFirstName(), "Василий");
    }

    @Test
    public void testMakeAppointment() throws ServerException {

        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse response1 = insertDoctor1(tokenAdmin);
        RegPatientDtoResponse response3 = insertPatientByService1();

        int docId1 = response1.getId();

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("faffasf", "fefefefefefef");
        String token = userService.login(loginDtoRequest);

        MakeAppointmentDtoRequest makeAppointmentDtoRequest = new MakeAppointmentDtoRequest(docId1, "", "08-06-2020","09:00");

        MakeAppointmentDtoResponse response = patientService.makeAppointment(makeAppointmentDtoRequest, token);

        assertTrue(response.getTicket().contains("08062020"));

    }

    @Test
    public Doctor testGetDoctorByDoctorId(int doctorId) {
        return null;
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

    //TODO test for busy room

//    @Test
//    public void testRegisterCommission() throws ServerException {
//        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
//        String tokenAdmin = userService.login(loginAdmin);
//
//        RegDoctorDtoResponse response1 = insertDoctor1(tokenAdmin);
//        RegDoctorDtoResponse response2 = insertDoctor2(tokenAdmin);
//
//        List<Integer> docIds = new ArrayList<>();
//        docIds.add(1);
//        docIds.add(2);
//        RegCommissionDtoRequest request = new RegCommissionDtoRequest(1, docIds, "302a", "08-06-2020", "10:00", 30);
//        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("eregergwww", "vdfvffvdfv");
//        String token = userService.login(loginDtoRequest);
//        doctorService.registerCommission(request, token);
//    }
//
//    @Test
//    public void testGetPatientTickets() throws ServerException {
////        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
////        LocalDate date = LocalDate.parse("272-02-2020", formatterDate);
//        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("faffasf", "fefefefefefef");
//        String token = userService.login(loginDtoRequest);
//        GetTicketsDtoResponse f = patientService.getTickets(token);
//        System.out.println();
//    }



}

