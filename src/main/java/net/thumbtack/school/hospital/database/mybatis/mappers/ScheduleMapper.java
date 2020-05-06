package net.thumbtack.school.hospital.database.mybatis.mappers;

import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Slot;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ScheduleMapper {

    @Insert("INSERT INTO slot_schedule (slot_start, slot_end, date_id) VALUES (#{slot.timeStart}, #{slot.timeEnd}, #{dateId})")
    @Options(useGeneratedKeys = true, keyProperty = "slot.id")
    public void insertSlot(@Param("slot") Slot slot, @Param("dateId") int dateId);

    @Insert("INSERT INTO date_schedule (date, doctor_id) VALUES (#{daySchedule.date}, #{doctorId})")
    @Options(useGeneratedKeys = true, keyProperty = "daySchedule.id")
    public void insertDaySchedule(@Param("daySchedule") DaySchedule daySchedule, @Param("doctorId") int doctorId);

    @Select("SELECT id FROM slot_schedule WHERE slot_start = #{timeStart} AND date_id IN (SELECT id FROM date_schedule WHERE date = #{date} AND doctor_id = #{doctorId})")
    int getSlotIdByDateTime(@Param("doctorId") int doctorId, @Param("date") LocalDate date, @Param("timeStart") LocalTime timeStart);
}
