package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.database.mybatis.mappers.*;
import net.thumbtack.school.hospital.database.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

public class DaoImplBase {

    private static boolean setUpIsDone = false;

    public static void setUp() {
        if (!setUpIsDone) {
            boolean initSqlSessionFactory = MyBatisUtils.initSqlSessionFactory();
            if (!initSqlSessionFactory) {
                throw new RuntimeException("Can't create connection, stop");
            }
            setUpIsDone = true;
        }
    }

    protected SqlSession getSession() {
        setUp();
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }

    protected UserMapper getUserMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(UserMapper.class);
    }

    protected AdminMapper getAdminMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(AdminMapper.class);
    }

    protected DoctorMapper getDoctorMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(DoctorMapper.class);
    }

    protected PatientMapper getPatientMapper(SqlSession sqlSession) { return sqlSession.getMapper(PatientMapper.class); }

    protected ScheduleMapper getScheduleMapper(SqlSession sqlSession) { return sqlSession.getMapper(ScheduleMapper.class); }

}
