package net.thumbtack.school.hospital;

import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.ScheduleDao;
import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.daoimpl.DoctorDaoImpl;
import net.thumbtack.school.hospital.daoimpl.ScheduleDaoImpl;
import net.thumbtack.school.hospital.debug.DebugDaoImpl;
import net.thumbtack.school.hospital.daoimpl.UserDaoImpl;
import net.thumbtack.school.hospital.database.mybatis.utils.MyBatisUtils;
import net.thumbtack.school.hospital.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class TestDao {

    protected UserDao userDao = new UserDaoImpl();
    protected DebugDaoImpl debugDao = new DebugDaoImpl();
    protected AdminDao adminDao = new AdminDaoImpl();
    protected DoctorDao doctorDao = new DoctorDaoImpl();
    protected ScheduleDao scheduleDao = new ScheduleDaoImpl();
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

    private void insertTestData() {
        User user1 = new User("Ivan", "Ivanov", "ivan322", "qwerty");
        Admin admin1 = new Admin("Ivan", "Ivanov", "ivan322", "qwerty", "junior admin");
        Doctor doctor1 = new Doctor("John", "Vatson", "rgerg43", "qwerty", "therapist", "302a");

    }

    @Test
    public void testUserInsert() {
        User user1 = new User("Ivan", "Ivanov", "ivan322", "qwerty");
        userDao.insertUser(user1, "admin");
        assertEquals(user1, userDao.getUser(user1.getLogin()));
    }

    @Test
    public void testAdminInsert() {
        Admin admin1 = new Admin("Ivan", "Ivanov", "ivan322", "qwerty", "junior admin");
        adminDao.insertAdmin(admin1);
        assertEquals(admin1, adminDao.getAdminByLogin(admin1.getLogin()));
    }

    @Test
    public void testUserDelete() {
        User user1 = new User("Ivan", "Ivanov", "ivan322", "qwerty");
        userDao.insertUser(user1, "admin");
        userDao.deleteUser(user1);
        assertNull(userDao.getUser(user1.getLogin()));
    }

    @Test
    public void testUserGet() {
        User user1 = new User("Ivan", "Ivanov", "ivan322", "qwerty");
        userDao.insertUser(user1, "admin");
        User user2 = userDao.getUser(user1.getLogin());
        assertEquals(user1, user2);
    }

    @Test
    public void testAdminGet() {
        Admin admin1 = new Admin("Ivan", "Ivanov", "ivan322", "qwerty", "junior admin");
        adminDao.insertAdmin(admin1);
        Admin admin2 = adminDao.getAdminByLogin(admin1.getLogin());
        assertEquals(admin1, admin2);
    }

    @Test
    public void testDoctorInsert() {
        Doctor doctor1 = new Doctor("John", "Vatson", "rgerg43", "qwerty", "therapist", "302a");
        doctorDao.insertDoctor(doctor1);
        Doctor doctor2 = doctorDao.getDoctorByLogin(doctor1.getLogin());
        assertEquals(doctor1, doctor2);
    }


    @Test
    public void testDoctorInsertWithSchedule1() {
        Slot slot1 = new Slot(LocalTime.of(11,0), LocalTime.of(11,14));
        Slot slot2 = new Slot(LocalTime.of(11,15), LocalTime.of(11,29));
        Slot slot3 = new Slot(LocalTime.of(14,0), LocalTime.of(14,14));
        Slot slot4 = new Slot(LocalTime.of(14,15), LocalTime.of(14,29));
        List<Slot> slots1 = new ArrayList<>();
        slots1.add(slot1);
        slots1.add(slot2);
        List<Slot> slots2 = new ArrayList<>();
        slots2.add(slot3);
        slots2.add(slot4);
        DaySchedule daySchedule1 = new DaySchedule(LocalDate.of(2020, 5, 2), slots1);
        DaySchedule daySchedule2 = new DaySchedule(LocalDate.of(2020,5,3), slots2);
        List<DaySchedule> dayScheduleList = new ArrayList<>();
        dayScheduleList.add(daySchedule1);
        dayScheduleList.add(daySchedule2);
        Doctor doctor1 = new Doctor("John", "Watson", "rgerg43", "qwerty", "therapist", "302a");
        doctor1.setSchedule(dayScheduleList);

        doctorDao.insertDoctor(doctor1);
        scheduleDao.insert(doctor1, daySchedule1);
        scheduleDao.insert(doctor1, daySchedule2);
        scheduleDao.insert(daySchedule1, slot1);
        scheduleDao.insert(daySchedule1, slot2);
        scheduleDao.insert(daySchedule2, slot3);
        scheduleDao.insert(daySchedule2, slot4);

        Doctor doctorFromDb = doctorDao.getDoctorByLogin(doctor1.getLogin());
        assertEquals(doctor1.getLastName(), doctorFromDb.getLastName());
    }

    @Test
    public void testDoctorInsertWithSchedule2() {
        Slot slot1 = new Slot(LocalTime.of(11,0), LocalTime.of(11,14));
        Slot slot2 = new Slot(LocalTime.of(11,15), LocalTime.of(11,29));
        Slot slot3 = new Slot(LocalTime.of(14,0), LocalTime.of(14,14));
        Slot slot4 = new Slot(LocalTime.of(14,15), LocalTime.of(14,29));
        List<Slot> slots1 = new ArrayList<>();
        slots1.add(slot1);
        slots1.add(slot2);
        List<Slot> slots2 = new ArrayList<>();
        slots2.add(slot3);
        slots2.add(slot4);
        DaySchedule daySchedule1 = new DaySchedule(LocalDate.of(2020, 5, 2), slots1);
        DaySchedule daySchedule2 = new DaySchedule(LocalDate.of(2020,5,3), slots2);
        List<DaySchedule> dayScheduleList = new ArrayList<>();
        dayScheduleList.add(daySchedule1);
        dayScheduleList.add(daySchedule2);
        Doctor doctor1 = new Doctor("John", "Watson", "rgerg43", "qwerty", "therapist", "302a");
        doctor1.setSchedule(dayScheduleList);

        doctorDao.insertDoctorWithSchedule(doctor1);

        Doctor doctorFromDb = doctorDao.getDoctorByLogin(doctor1.getLogin());
        assertEquals(doctor1.getLastName(), doctorFromDb.getLastName());
    }



}
