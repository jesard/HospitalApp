package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminDaoImpl extends UserDaoImpl implements AdminDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Override
    public Admin insertAdmin(Admin admin) {
        LOGGER.debug("DAO insert Admin {}", admin);
        insertUser(admin, "admin");
        try (SqlSession sqlSession = getSession()) {
            try {
                getAdminMapper(sqlSession).insert(admin);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Admin {}: {}", admin, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return admin;
    }

    @Override
    public Admin getAdminByLogin(String login) {
        LOGGER.debug("DAO get Admin with login: {}", login);
        User user = getUser(login);
        try (SqlSession sqlSession = getSession()) {
            try {
                String position = getAdminMapper(sqlSession).getPosition(user.getUserId());
                return new Admin(user, position);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get Admin with login {}: {}", login, ex);
                throw ex;
            }
        }
    }

    @Override
    public Admin getAdminById(int id) {
        return null;
    }

}
