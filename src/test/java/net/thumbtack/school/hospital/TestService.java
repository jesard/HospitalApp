package net.thumbtack.school.hospital;

import net.thumbtack.school.hospital.Service.AdminService;
import net.thumbtack.school.hospital.Service.DoctorService;
import net.thumbtack.school.hospital.Service.PatientService;
import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.database.mybatis.utils.MyBatisUtils;
import net.thumbtack.school.hospital.debug.DebugDaoImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestService {

    private AdminService adminService = new AdminService();
    private DoctorService doctorService = new DoctorService();
    private PatientService patientService = new PatientService();
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
        String regDocJson2 = "{" +
                "\"firstName\": \"John\"," +
                "\"lastName\": \"Watson\"," +
                "\"patronymic\": \"отчество\"," +
                "\"speciality\":\"therapist\"," +
                "\"room\":\"555a\"," +
                "\"login\":\"login444\"," +
                "\"password\":\"qwerty\"," +
                "\"dateStart\":\"08-05-2020\"," +
                "\"dateEnd\":\"10-06-2020\"," +
                "\"weekSchedule\":" +
                "{}," +
                "\"weekDaysSchedule\" : [{\"weekDay\":\"Mon\",\"timeStart\":\"08:00\",\"timeEnd\":\"12:00\"}, {\"weekDay\":\"Tue\",\"timeStart\":\"14:00\",\"timeEnd\":\"18:00\"}]," +
                "\"duration\": 15}";

        String response1 = doctorService.registerDoctor(regDocJson1);
        String response2 = doctorService.registerDoctor(regDocJson2);
        assertTrue(response1.contains("09:00"));
        assertTrue(response2.contains("08:00"));

    }

    @Test
    public void testPatientRegister() {
        String regPatientJson1 = "{\"firstName\": \"имя\"," +
                "    \"lastName\": \"фамилия\"," +
                "    \"patronymic\": \"отчество\"," +
                "    \"email\": \"адрес\"," +
                "    \"address\": \"домашний адрес, одной строкой\"," +
                "    \"phone\": \"номер\"," +
                "    \"login\": \"логин\"," +
                "    \"password\": \"пароль\"}";
        String response = patientService.registerPatient(regPatientJson1);
        assertTrue(response.contains("фамилия"));
    }

}

