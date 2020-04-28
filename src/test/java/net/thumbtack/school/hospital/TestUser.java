package net.thumbtack.school.hospital;

import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.daoimpl.DebugDao;
import net.thumbtack.school.hospital.daoimpl.UserDaoImpl;
import net.thumbtack.school.hospital.database.mybatis.utils.MyBatisUtils;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class TestUser {

    protected UserDao userDao = new UserDaoImpl();
    protected DebugDao debugDao = new DebugDao();
    protected AdminDao adminDao = new AdminDaoImpl();
    private static boolean setUpIsDone = false;

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
    public void testUserInsert() {
        User user1 = new User("Ivan", "Ivanov", "ivan322", "qwerty");
        userDao.insertUser(user1, "admin");
        assertEquals(user1, userDao.getUser(user1.getLogin()));
    }

    @Test
    public void testAdminInsert() {
        Admin admin1 = new Admin("Ivan", "Ivanov", "ivan322", "qwerty", "junior admin");
        adminDao.insertAdmin(admin1);
        assertEquals(admin1, adminDao.getAdmin(admin1.getLogin()));
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
        Admin admin2 = adminDao.getAdmin(admin1.getLogin());
        assertEquals(admin1, admin2);
    }


}
