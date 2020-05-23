package net.thumbtack.school.hospital.daoimpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.error.Field;
import net.thumbtack.school.hospital.error.MyError;
import net.thumbtack.school.hospital.error.ServerErrorCode;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDaoImpl extends DaoImplBase implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void insertUser(User user, String descriptor) throws ServerException {
        LOGGER.debug("DAO insert User {}", user.getLogin());
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insert(user, descriptor);
            } catch (PersistenceException ex) {
                LOGGER.info("Can't insert User {} {}", user.getLogin(), ex);
                sqlSession.rollback();
                if (ex.getCause() instanceof MySQLIntegrityConstraintViolationException) {
                    throw new ServerException(new MyError(ServerErrorCode.USER_DUPLICATE, Field.LOGIN, user.getLogin()));
                } else
                    throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public User getUserByLogin(String login) throws ServerException {
        LOGGER.debug("DAO get User with login: {}", login);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).getUser(login);
            } catch (BindingException ex) {
                LOGGER.info("Can't get User with login {}: {}", login, ex);
                throw new ServerException(new MyError(ServerErrorCode.USER_NOT_FOUND, Field.LOGIN, login));
            } catch (PersistenceException ex) {
                LOGGER.info("Can't get User with login {}: {}", login, ex);
                throw ex;
            }
        }
    }

    @Override
    public User getUserById(int id) throws ServerException {
        LOGGER.debug("DAO get User with id: {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).getById(id);
            } catch (BindingException ex) {
                LOGGER.info("Can't delete User with login {}: {}", id, ex);
                throw new ServerException(new MyError(ServerErrorCode.USER_NOT_FOUND, Field.COOKIE));
            } catch (PersistenceException ex) {
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
            } catch (PersistenceException ex) {
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
            } catch (PersistenceException ex) {
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
            } catch (PersistenceException ex) {
                LOGGER.info("Can't login User with id {} - {}", userId, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void logout(String token) {
        LOGGER.debug("DAO logout user with token: {}", token);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).logout(token);
            } catch (PersistenceException ex) {
                LOGGER.info("Can't get UserId with token {} - {}", token, ex);
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public int getUserIdByToken(String token) throws ServerException {
        LOGGER.debug("DAO get UserId with token: {}", token);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).getUserIdByToken(token);
            } catch (BindingException ex) {
                LOGGER.info("Can't find UserId with token {} - {}", token, ex);
                throw new ServerException(new MyError(ServerErrorCode.USER_NOT_FOUND, Field.COOKIE));
            } catch (PersistenceException ex) {
                LOGGER.info("Can't get UserId with token {} - {}", token, ex);
                throw new ServerException(new MyError(ServerErrorCode.DATABASE_ERROR, Field.UNKNOWN));
            }
        }
    }

    @Override
    public String getDescriptorByUserId(int userId) throws ServerException {
        LOGGER.debug("DAO get Descriptor By User Id: {}", userId);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).getDescriptorByUserId(userId);
            } catch (BindingException ex) {
                LOGGER.info("Can't get Descriptor By User Id: {} - {}", userId, ex);
                throw new ServerException(new MyError(ServerErrorCode.USER_NOT_FOUND, Field.COOKIE));
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get Descriptor By User Id: {} - {}", userId, ex);
                throw ex;
            }
        }
    }





}
