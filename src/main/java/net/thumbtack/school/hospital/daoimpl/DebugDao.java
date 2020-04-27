package net.thumbtack.school.hospital.daoimpl;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugDao extends DaoImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebugDao.class);

    public void deleteAllUsers() {
        LOGGER.debug("DAO delete all Users");
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).deleteAll();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete all Users", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

}
