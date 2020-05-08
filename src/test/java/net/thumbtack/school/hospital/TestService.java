package net.thumbtack.school.hospital;

import com.google.gson.Gson;
import net.thumbtack.school.hospital.Service.AdminService;
import net.thumbtack.school.hospital.Service.DoctorService;
import net.thumbtack.school.hospital.Service.PatientService;
import net.thumbtack.school.hospital.Service.UserService;
import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.database.mybatis.utils.MyBatisUtils;
import net.thumbtack.school.hospital.debug.DebugDaoImpl;
import net.thumbtack.school.hospital.dto.response.regDoctor.RegDoctorDtoResponse;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestService {

    Gson gson = new Gson();
    private AdminService adminService = new AdminService();
    private DoctorService doctorService = new DoctorService();
    private PatientService patientService = new PatientService();
    private UserService userService = new UserService();
    protected DebugDaoImpl debugDao = new DebugDaoImpl();
    protected static boolean setUpIsDone = false;

    @BeforeAll()
    public static void setUp() {
        if (!setUpIsDone) {
            boolean initSqlSessionFactory = MyBatisUtils.initSqlSessionFactory();
            if (!initSqlSessionFactory) {
                throw new RuntimeException("Can't create connection, stop");
            }
            setUpIsDone = true;
        }
    }

    @BeforeEach()
    public void clearDatabase() {
        debugDao.deleteAllUsers();
    }

    private String insertDoctor1() {
    	// REVU не надо вручную создавать json
    	// работайте с DTO, заполняйте и получайте
        String regDocJson1 = "{" +
                "\"firstName\": \"John\"," +
                " \"lastName\": \"Watson\"," +
                " \"patronymic\": \"отчество\"," +
                "\"speciality\":\"therapist\"," +
                "\"room\":\"555a\"," +
                "\"login\":\"login443\"," +
                "\"password\":\"qwerty\"," +
                "\"dateStart\":\"05-05-2020\"," +
                "\"dateEnd\":\"05-07-2020\"," +
                "\"weekSchedule\":" +
                "{" +
                "\"timeStart\" :\"09:00\"," +
                "\"timeEnd\":\"15:00\"," +
                "\"weekDays\":[]" +
                "}," +
                "\"weekDaysSchedule\":[]," +
                "\"duration\": 15}";
        return doctorService.registerDoctor(regDocJson1);
    }

    private String insertDoctor2() {
        String regDocJson2 = "{" +
                "\"firstName\": \"John\"," +
                "\"lastName\": \"Watson\"," +
                "\"patronymic\": \"отчество\"," +
                "\"speciality\":\"therapist\"," +
                "\"room\":\"555a\"," +
                "\"login\":\"login444\"," +
                "\"password\":\"qwerty\"," +
                "\"dateStart\":\"08-05-2020\"," +
                "\"dateEnd\":\"05-06-2020\"," +
                "\"weekSchedule\":" +
                "{}," +
                "\"weekDaysSchedule\" : [{\"weekDay\":\"Mon\",\"timeStart\":\"08:00\",\"timeEnd\":\"10:00\"}, {\"weekDay\":\"Tue\",\"timeStart\":\"14:00\",\"timeEnd\":\"16:00\"}]," +
                "\"duration\": 15}";
        return doctorService.registerDoctor(regDocJson2);
    }

    private String insertPatient1() {
        String regPatientJson1 = "{\"firstName\": \"имя\"," +
                "    \"lastName\": \"фамилия\"," +
                "    \"patronymic\": \"отчество\"," +
                "    \"email\": \"адрес\"," +
                "    \"address\": \"домашний адрес, одной строкой\"," +
                "    \"phone\": \"номер\"," +
                "    \"login\": \"логин\"," +
                "    \"password\": \"пароль\"}";
        return patientService.registerPatient(regPatientJson1);
    }

    @Test
    public void testAdminRegister() {
        String regAdminJson1 = "{\"firstName\": \"имяAdmin\"," +
                "    \"lastName\": \"фамилия\"," +
                "    \"patronymic\": \"отчество\"," +
                "    \"position\": \"должность\"," +
                "    \"login\": \"логин\"," +
                "    \"password\": \"пароль\"}";

        String response = adminService.registerAdmin(regAdminJson1);
        assertTrue(response.contains("имяAdmin"));
    }

    @Test
    public void testDoctorRegister() {

        String response1 = insertDoctor1();
        String response2 = insertDoctor2();
        assertTrue(response1.contains("09:00"));
        assertTrue(response2.contains("08:00"));

    }

    @Test
    public void testPatientRegister() {
        String response = insertPatient1();
        assertTrue(response.contains("фамилия"));
    }

    @Test
    public void testMakeAppointment() {

        String response1 = insertDoctor1();
        String response3 = insertPatient1();

        int docId1 = gson.fromJson(response1, RegDoctorDtoResponse.class).getId();


        String loginJson = "{\"login\": \"логин\"," +
                "\"password\": \"пароль\"}";
        String token = userService.login(loginJson);

        String makeAppointmentJson = "{\"doctorId\":" + docId1 + "," +
                "\"speciality\":\"\"," +
                "\"date\":\"05-05-2020\"," +
                "\"time\":\"09:00\"}";

        String response = patientService.makeAppointment(makeAppointmentJson, token);
        assertTrue(response.contains("\"ticket\":\"D"));
    }

    @Test
    public void testGetDoctorByToken() {
        insertDoctor2();

        String loginJson = "{\"login\": \"login444\"," +
                "\"password\": \"qwerty\"}";

        String token1 = userService.login(loginJson);
        String response = userService.getUserByToken(token1);
        assertTrue(response.contains("daySchedule"));
        assertTrue(response.contains("Watson"));
    }

    @Test
    public void testGetPatientByToken() {
        insertPatient1();

        String loginJson = "{\"login\": \"логин\"," +
                "\"password\": \"пароль\"}";

        String token1 = userService.login(loginJson);
        String response = userService.getUserByToken(token1);

        assertTrue(response.contains("адрес"));
        assertTrue(response.contains("номер"));
    }

}

