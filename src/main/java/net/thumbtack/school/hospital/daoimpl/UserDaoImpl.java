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
        LOGGER.debug("DAO insert User {}", user.getLogin());
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insert(user, descriptor);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert User {} {}", user.getLogin(), ex);
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
    public User getUserById(int id) {
        LOGGER.debug("DAO get User with id: {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).getById(id);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete User with login {}: {}", id, ex);
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
    public void updateUser(User newUser) {
        LOGGER.debug("DAO update User with id {}", newUser.getUserId());
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).update(newUser);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't update User with id {}", newUser.getUserId());
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void login(int userId, String token) {
        LOGGER.debug("DAO login User with id {}", userId);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).login(userId, token);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't login User with id {} - {}", userId, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public int getUserIdByToken(String token) {
        LOGGER.debug("DAO get UserId with token: {}", token);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).getUserIdByToken(token);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get UserId with token {} - {}", token, ex);
                throw ex;
            }
        }
    }

    @Override
    public String getDescriptorByUserId(int userId) {
        LOGGER.debug("DAO get Descriptor By User Id: {}", userId);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).getDescriptorByUserId(userId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get Descriptor By User Id: {} - {}", userId, ex);
                throw ex;
            }
        }
    }



}
