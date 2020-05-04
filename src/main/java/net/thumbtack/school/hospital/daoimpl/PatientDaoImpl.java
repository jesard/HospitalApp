package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.PatientDao;
import net.thumbtack.school.hospital.model.Patient;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientDaoImpl extends UserDaoImpl implements PatientDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientDaoImpl.class);

    @Override
    public Patient insertPatient(Patient patient) {
        LOGGER.debug("DAO insert Patient {}", patient);
        insertUser(patient, "patient");
        try (SqlSession sqlSession = getSession()) {
            try {
                getPatientMapper(sqlSession).insert(patient);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Patient {}: {}", patient, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return patient;
    }

    @Override
    public Patient getPatientById(int id) {
        return null;
    }
}
