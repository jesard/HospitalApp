package net.thumbtack.school.hospital.testdao;

import net.thumbtack.school.hospital.TestBase;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class TestUserDao extends TestBase {

    @Test
    public void testUserInsert() throws ServerException {
        User user = new User("Ivan", "Ivanov", "","ivan322", "qwerty");
        userDao.insertUser(user, "admin");
        assertEquals(user, userDao.getUserByLogin(user.getLogin()));
    }

    @Test
    public void testGetUserByLogin() throws ServerException {
        User user = insertUser1();
        User userFromDb = userDao.getUserByLogin(user.getLogin());
        assertEquals(user, userFromDb);
    }

    @Test
    public void testGetUserByUserId() throws ServerException {
        User user = insertUser1();
        User userFromDb = userDao.getUserById(user.getUserId());
        assertEquals(user, userFromDb);
    }

    @Test
    public void testUserDelete() throws ServerException {
        User user = insertUser1();
        userDao.deleteUser(user);
        assertNull(userDao.getUserByLogin(user.getLogin()));
    }

    @Test
    public void testUpdateUser() throws ServerException {
        User user = insertUser1();
        user.setFirstName("НовоеИмя");
        user.setLastName("НоваяФамилия");
        user.setPatronymic("НовоеОтч");
        user.setLogin("newLog");
        user.setPassword("newPassword");
        userDao.updateUser(user);
        User userFromDb = userDao.getUserById(user.getUserId());
        assertEquals(userFromDb.getLastName(), user.getLastName());
        assertEquals(userFromDb.getPassword(), user.getPassword());
        assertNotEquals(user.getLogin(), userFromDb.getLogin());
    }

    @Test
    public void testLogin() throws ServerException {
        User user = insertUser1();
        String token = UUID.randomUUID().toString();
        userDao.login(user.getUserId(), token);
        assertEquals(user.getUserId(), userDao.getUserIdByToken(token));
        assertThrows(RuntimeException.class, () -> userDao.login(user.getUserId(), token));
        assertThrows(RuntimeException.class, () -> userDao.login(0, "token"));
    }

    @Test
    public void testLogout() throws ServerException {
        User user = insertUser1();
        String token = UUID.randomUUID().toString();
        userDao.login(user.getUserId(), token);
        userDao.logout(token);
        assertThrows(ServerException.class, () -> userDao.getUserIdByToken(token));
    }

    @Test
    public void testGetDescriptorByUserId() throws ServerException {
        User user = insertUser1();
        assertEquals("admin", userDao.getDescriptorByUserId(user.getUserId()));
    }






}
