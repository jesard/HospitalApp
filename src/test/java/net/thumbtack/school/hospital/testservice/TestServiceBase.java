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
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

    protected RegPatientDtoResponse insertPatientByService1() throws ServerException {
        RegPatientDtoRequest regPatientDtoRequest = new RegPatientDtoRequest("Василий","Мураев", "Иванович", "efef@wefwe.ru", "frfrferf", "489348", "faffasf", "fefefefefefef");
        return patientService.registerPatient(regPatientDtoRequest);
    }

    protected RegPatientDtoResponse insertPatientByService2() throws ServerException {
        RegPatientDtoRequest regPatientDtoRequest = new RegPatientDtoRequest("Евгений","Карузо", "Петрович", "fmdko@wefwe.ru", "ививаи", "56456456", "kkkkiiky", "rfgergergwq");
        return patientService.registerPatient(regPatientDtoRequest);
    }

    protected MakeAppointmentDtoResponse makeAppointment(int doctorId, String date, String time, String patientToken) throws ServerException {
        MakeAppointmentDtoRequest makeAppointmentDtoRequest = new MakeAppointmentDtoRequest(doctorId, "", date,time);
        return patientService.makeAppointment(makeAppointmentDtoRequest, patientToken);
    }

    protected MakeAppointmentDtoResponse makeAppointment(String speciality, String date, String time, String patientToken) throws ServerException {
        MakeAppointmentDtoRequest makeAppointmentDtoRequest = new MakeAppointmentDtoRequest(0, speciality, date,time);
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
    public void testDoctorRegister() throws ServerException {

        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);

        RegDoctorDtoResponse response = insertDoctor2(tokenAdmin);
        assertEquals(response.getFirstName(), "Марат");
        assertEquals(response.getLastName(), "Веретенников");
        LocalDate dateFromDb = LocalDate.parse(response.getSchedule().get(0).getDate(), formatterDate);
        assertEquals(DayOfWeek.MONDAY, dateFromDb.getDayOfWeek());
    }

    @Test
    public void testLoginPasswordCaseSensitivity() throws ServerException {
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
    public void testLogin() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);

        RegPatientDtoResponse responsePatient = insertPatientByService1();
        RegDoctorDtoResponse responseDoc = insertDoctor2(tokenAdmin);

        LoginDtoRequest loginPatient = new LoginDtoRequest("faffasf", "fefefefefefef");
        String tokenPatient = userService.login(loginPatient);

        LoginDtoRequest loginDoc = new LoginDtoRequest("eregergwww", "vdfvffvdfv");
        String tokenDoc = userService.login(loginDoc);

        assertNotEquals(tokenDoc, tokenAdmin);
        assertNotEquals(tokenDoc, tokenPatient);
        assertEquals(tokenPatient.length(), tokenAdmin.length());
        assertEquals(tokenDoc.length(), tokenPatient.length());
    }

    @Test
    public void testLoginSecondTime() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        String tokenAdmin2 = userService.login(loginAdmin);
        assertNotEquals(tokenAdmin, tokenAdmin2);
    }

    @Test
    public void testLogout() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        userService.logout(tokenAdmin);
        try {
            userService.logout(tokenAdmin);
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.USER_NOT_FOUND, ex.getErrors().get(0).getErrorCode());
        }
    }

    @Test
    public void testGetUserByToken() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);

        insertPatientByService1();
        insertDoctor2(tokenAdmin);

        LoginDtoRequest loginPatient = new LoginDtoRequest("faffasf", "fefefefefefef");
        String tokenPatient = userService.login(loginPatient);
        LoginDtoRequest loginDoc = new LoginDtoRequest("eregergwww", "vdfvffvdfv");
        String tokenDoc = userService.login(loginDoc);

        User userAdmin = userService.getUserByToken(tokenAdmin);
        User userPatient = userService.getUserByToken(tokenPatient);
        User userDoc = userService.getUserByToken(tokenDoc);

        assertNotNull(((Admin) userAdmin).getPosition());
        assertNotNull(((Patient) userPatient).getEmail());
        assertNotNull(((Doctor) userDoc).getSpeciality());
    }

    @Test
    public void testGetSettings() {}

    @Test
    public void testGetStats() {}



















}

