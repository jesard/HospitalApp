package net.thumbtack.school.hospital;

import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.PatientDao;
import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.daoimpl.DoctorDaoImpl;
import net.thumbtack.school.hospital.daoimpl.PatientDaoImpl;
import net.thumbtack.school.hospital.daoimpl.UserDaoImpl;
import net.thumbtack.school.hospital.database.mybatis.utils.MyBatisUtils;
import net.thumbtack.school.hospital.debug.DebugDaoImpl;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TestBase {

    protected UserDao userDao = new UserDaoImpl();
    protected DebugDaoImpl debugDao = new DebugDaoImpl();
    protected AdminDao adminDao = new AdminDaoImpl();
    protected DoctorDao doctorDao = new DoctorDaoImpl();
    protected PatientDao patientDao = new PatientDaoImpl();
    protected static boolean setUpIsDone = false;

    protected final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    protected final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

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
        debugDao.deleteAllSessions();
    }

    protected List<DaySchedule> makeSchedule1() {
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
        return dayScheduleList;
    }

    protected List<DaySchedule> makeScheduleSameEveryDay(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int slotDuration) {
        DaySchedule daySchedule;
        List<DaySchedule> dayScheduleList = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            daySchedule = new DaySchedule();
            daySchedule.setDate(date);
            List<Slot> slots = new ArrayList<>();
            for (LocalTime time = startTime; time.plusMinutes(slotDuration).isBefore(endTime.plusMinutes(1)); time = time.plusMinutes(slotDuration)) {
                Slot slot = new Slot(time, time.plusMinutes(slotDuration - 1));
                slot.setDaySchedule(daySchedule);
                slots.add(slot);
            }
            daySchedule.setSlotSchedule(slots);
            dayScheduleList.add(daySchedule);
        }
        return dayScheduleList;
    }

    protected User insertUser1() throws ServerException {
        User user = new User("Ivan", "Ivanov", "","ivan322", "qwerty");
        userDao.insertUser(user, "admin");
        return user;
    }

    protected Admin insertAdmin1() throws ServerException {
        Admin admin = new Admin("Ivan", "Ivanov", "", "ivan322#", "qwerty", "junior admin");
        adminDao.insertAdmin(admin);
        return admin;
    }

    protected Doctor insertDoctorWithoutSchedule() throws ServerException {
        Doctor doctor = new Doctor("John", "Vatson", "", "rgerg43", "qwerty", "therapist", "302a");
        doctorDao.insertDoctor(doctor);
        return doctor;
    }

    protected Doctor insertDoctorWithSchedule1() throws ServerException {
        List<DaySchedule> schedule = makeSchedule1();
        Doctor doctor = new Doctor("John", "Watson", "", "rgerg43", "qwerty", "therapist", "302a");
        doctor.setSchedule(schedule);

        doctorDao.insertDoctor(doctor);
        return doctor;
    }

    protected Doctor insertDoctorWithSchedule2() throws ServerException {
        Slot slot1 = new Slot(LocalTime.of(10,0), LocalTime.of(10,14));
        Slot slot2 = new Slot(LocalTime.of(10,15), LocalTime.of(10,29));
        Slot slot3 = new Slot(LocalTime.of(12,0), LocalTime.of(12,14));
        Slot slot4 = new Slot(LocalTime.of(12,15), LocalTime.of(12,29));
        List<Slot> slots1 = new ArrayList<>();
        slots1.add(slot1);
        slots1.add(slot2);
        List<Slot> slots2 = new ArrayList<>();
        slots2.add(slot3);
        slots2.add(slot4);
        DaySchedule daySchedule1 = new DaySchedule(LocalDate.of(2020, 6, 2), slots1);
        DaySchedule daySchedule2 = new DaySchedule(LocalDate.of(2020,6,3), slots2);
        List<DaySchedule> dayScheduleList = new ArrayList<>();
        dayScheduleList.add(daySchedule1);
        dayScheduleList.add(daySchedule2);
        Doctor doctor = new Doctor("Василий", "Васиельв", "", "vever", "qwerty", "surgeon", "111");
        doctor.setSchedule(dayScheduleList);

        doctorDao.insertDoctor(doctor);
        return doctor;
    }

    protected Patient insertPatient1() throws ServerException {
        Patient patient = new Patient("Василий","Мураев", "Иванович", "ebberge", "frfrferf", "efef@wefwe.ru", "sdfsdfsdf", "42344234");
        return patientDao.insertPatient(patient);
    }

    protected Patient insertPatient2() throws ServerException {
        Patient patient = new Patient("Андрей","Сахаров", "Дмитриевич", "gwgwegg", "qwerty", "fwefw@we.ru", "wefwef", "545345");
        return patientDao.insertPatient(patient);
    }



}
