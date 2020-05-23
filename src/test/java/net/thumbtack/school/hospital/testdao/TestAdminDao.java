package net.thumbtack.school.hospital.testdao;

import net.thumbtack.school.hospital.TestBase;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.Admin;
import org.apache.ibatis.exceptions.PersistenceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestAdminDao extends TestBase {

    @Test
    public void testAdminInsert() throws ServerException {
        Admin admin1 = new Admin("Ivan", "Ivanov", "", "ivan322#", "qwerty", "junior admin");
        adminDao.insertAdmin(admin1);
        assertEquals(admin1, adminDao.getAdminByUserId(admin1.getUserId()));
    }

    @Test
    public void testAdminInsertSameLogin() throws ServerException {
        Admin admin1 = new Admin("Ivan", "Ivanov", "", "ivan322#", "qwerty", "junior admin");
        adminDao.insertAdmin(admin1);
        try {
            adminDao.insertAdmin(admin1);
        } catch (ServerException ex) {
            assertTrue(ex.getErrors().get(0).getMessage().contains("duplicate"));
        }
    }

    @Test
    public void testAdminUpdate() throws ServerException {
        Admin admin = insertAdmin1();
        admin.setFirstName("НовыйАдмин");
        admin.setLastName("Фамилия");
        admin.setPatronymic("Отчество");
        admin.setLogin("newLogin");
        admin.setPassword("43fkejw");
        admin.setPosition("middle admin");
        adminDao.updateAdmin(admin);
        Admin adminFromDb = adminDao.getAdminByUserId(admin.getUserId());
        assertEquals(admin, adminFromDb);
        assertNotEquals(admin.getLogin(), adminFromDb.getLogin());
    }

    @Test
    public void testGetAdminByUserId() throws ServerException {
        Admin admin = insertAdmin1();
        Admin adminFromDb = adminDao.getAdminByUserId(admin.getUserId());
        assertThrows(RuntimeException.class, () -> adminDao.getAdminByUserId(0));
        assertEquals(admin, adminFromDb);
    }

}
