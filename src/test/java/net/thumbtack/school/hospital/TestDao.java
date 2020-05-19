package net.thumbtack.school.hospital;

import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class TestDao extends TestBase {

    @Test
    public void testUserInsert() {
        User user1 = new User("Ivan", "Ivanov", "","ivan322", "qwerty");
        userDao.insertUser(user1, "admin");
        assertEquals(user1, userDao.getUser(user1.getLogin()));
    }

    @Test
    public void testUserDelete() {
        User user1 = new User("Ivan", "Ivanov", "", "ivan322", "qwerty");
        userDao.insertUser(user1, "admin");
        userDao.deleteUser(user1);
        assertNull(userDao.getUser(user1.getLogin()));
    }

    @Test
    public void testAdminInsert() {
        Admin admin1 = new Admin("Ivan", "Ivanov", "", "ivan322#", "qwerty", "junior admin");
        adminDao.insertAdmin(admin1);
        assertEquals(admin1, adminDao.getAdminByUserId(admin1.getUserId()));
    }

    @Test
    public void testAdminUpdate() {
        Admin admin1 = new Admin("Ivan", "Ivanov", "", "ivan322#", "qwerty", "junior admin");
        adminDao.insertAdmin(admin1);
        admin1.setFirstName("НовыйАдмин");
        admin1.setLastName("Фамилия");
        admin1.setPatronymic("Отчество");
        admin1.setPassword("43fkejw");
        admin1.setPosition("middle admin");
        adminDao.updateAdmin(admin1);
        assertEquals(admin1, adminDao.getAdminByUserId(admin1.getUserId()));
    }


}
