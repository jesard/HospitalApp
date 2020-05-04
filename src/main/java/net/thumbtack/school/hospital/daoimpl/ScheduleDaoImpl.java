package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.ScheduleDao;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Slot;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ScheduleDaoImpl extends DaoImplBase implements ScheduleDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleDaoImpl.class);

    @Override
    public DaySchedule insert(Doctor doctor, DaySchedule daySchedule) {
        LOGGER.debug("DAO insert DaySchedule {}", daySchedule);
        try (SqlSession sqlSession = getSession()) {
            try {
                int doctorId = doctor.getId();
                getScheduleMapper(sqlSession).insertDaySchedule(daySchedule, doctorId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert DaySchedule {}: {}", daySchedule, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return daySchedule;
    }


    @Override
    public Slot insert(DaySchedule daySchedule, Slot slot) {
        LOGGER.debug("DAO insert Slot {}", slot);
        try (SqlSession sqlSession = getSession()) {
            try {
                int dateId = daySchedule.getId();
                getScheduleMapper(sqlSession).insertSlot(slot, dateId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Slot {}: {}", slot, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return slot;
    }


}
