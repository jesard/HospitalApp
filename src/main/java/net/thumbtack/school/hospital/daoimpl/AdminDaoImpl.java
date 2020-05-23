package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.error.Field;
import net.thumbtack.school.hospital.error.MyError;
import net.thumbtack.school.hospital.error.ServerErrorCode;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminDaoImpl extends UserDaoImpl implements AdminDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Override
    public Admin insertAdmin(Admin admin) throws ServerException {
        LOGGER.debug("DAO insert Admin {}", admin);
        insertUser(admin, "admin");
        try (SqlSession sqlSession = getSession()) {
            try {
                getAdminMapper(sqlSession).insert(admin);
            } catch (PersistenceException ex) {
                LOGGER.info("Can't insert Admin {}: {}", admin, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return admin;
    }

    @Override
    public Admin getAdminByUserId(int userId) throws ServerException {
        LOGGER.debug("DAO get Admin with id: {}", userId);
        User user = getUserById(userId);
        try (SqlSession sqlSession = getSession()) {
            try {
                Admin admin = getAdminMapper(sqlSession).getAdminByUserId(userId);
                admin.setUserId(userId);
                admin.setFirstName(user.getFirstName());
                admin.setLastName(user.getLastName());
                admin.setPatronymic(user.getPatronymic());
                admin.setLogin(user.getLogin());
                admin.setPassword(user.getPassword());
                return admin;
            } catch (BindingException ex) {
                LOGGER.info("Can't get Admin with id {}: {}", userId, ex);
                throw new ServerException(new MyError(ServerErrorCode.USER_NOT_FOUND, Field.COOKIE));
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get Admin with id {}: {}", userId, ex);
                throw ex;
            }
        }
    }

    @Override
    public void updateAdmin(Admin newAdmin) {
        LOGGER.debug("DAO update Admin with id = {}", newAdmin.getUserId());
        updateUser(newAdmin);
        try (SqlSession sqlSession = getSession()) {
            try {
                getAdminMapper(sqlSession).update(newAdmin);
            } catch (PersistenceException ex) {
                LOGGER.info("Can't update Admin with id = {}", newAdmin.getUserId(), ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

}
