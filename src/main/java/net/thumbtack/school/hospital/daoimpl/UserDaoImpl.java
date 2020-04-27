package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDaoImpl extends DaoImplBase implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void insertUser(User user, String descriptor) {
        LOGGER.debug("DAO insert User {}", user);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insert(user, descriptor);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert User {} {}", user, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public User getUser(String login) {
        LOGGER.debug("DAO get User with login: {}", login);
        try (SqlSession sqlSession = getSession()) {
            try {
            return getUserMapper(sqlSession).get(login);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete User with login {}: {}", login, ex);
            throw ex;
            }
        }
    }

    @Override
    public void deleteUser(User user) {
        LOGGER.debug("DAO delete User {} ", user);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).delete(user);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete User {}: {}", user, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }


    @Override
    public void addSession(String token, User user) {

    }

    @Override
    public User getSession(String token) {
        return null;
    }

    @Override
    public void removeSession(String token) {

    }
}
