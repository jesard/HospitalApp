package net.thumbtack.school.hospital.database.mybatis.mappers;

import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.Slot;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleMapper {

    @Insert("INSERT INTO slot_schedule (slot_start, slot_end, date_id) VALUES (#{slot.timeStart}, #{slot.timeEnd}, #{dateId})")
    @Options(useGeneratedKeys = true, keyProperty = "slot.id")
    public void insertSlot(@Param("slot") Slot slot, @Param("dateId") int dateId);

    @Insert("INSERT INTO date_schedule (date, doctor_id) VALUES (#{daySchedule.date}, #{doctorId})")
    @Options(useGeneratedKeys = true, keyProperty = "daySchedule.id")
    public void insertDaySchedule(@Param("daySchedule") DaySchedule daySchedule, @Param("doctorId") int doctorId);

    @Select("SELECT id FROM slot_schedule WHERE slot_start = #{timeStart} AND date_id IN (SELECT id FROM date_schedule WHERE date = #{date} AND doctor_id = #{doctorId})")
    int getSlotIdByDateTime(@Param("doctorId") int doctorId, @Param("date") LocalDate date, @Param("timeStart") LocalTime timeStart);

    @Select("SELECT ticket_number FROM slot_schedule WHERE date_id = (SELECT id FROM date_schedule WHERE date = #{date} AND doctor_id = #{doctorId}) AND slot_start <= #{timeEnd} AND slot_end >= #{timeStart}")
    List<String> getTicketsByDateTimeRange(@Param("doctorId") int doctorId, @Param("date") LocalDate date, @Param("timeStart") LocalTime timeStart, @Param("timeEnd") LocalTime timeEnd);

    @Select("SELECT ticket_number, patient_id, date_id FROM slot_schedule WHERE date_id IN (SELECT id FROM date_schedule WHERE date >= #{date} AND doctor_id = #{doctorId}) AND ticket_number IS NOT NULL")
    @Results({
            @Result(property = "ticketNumber", column = "ticket_number"),
            @Result(property = "daySchedule", column = "date_id", javaType = DaySchedule.class,
                    one = @One(select = "net.thumbtack.school.hospital.database.mybatis.mappers.DoctorMapper.getDayScheduleById", fetchType = FetchType.LAZY)),
            @Result(property = "patient", column = "patient_id", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.database.mybatis.mappers.PatientMapper.getPatientByPatientId", fetchType = FetchType.LAZY)),
    })
    List<Slot> getTicketsAfterDate(@Param("doctorId") int doctorId, @Param("date") LocalDate date);

    @Select("SELECT id FROM slot_schedule WHERE date_id = (SELECT id FROM date_schedule WHERE date = #{date} AND doctor_id = #{doctorId}) AND slot_start <= #{timeEnd} AND slot_end >= #{timeStart}")
    List<Integer> getSlotIdsByDateTimeRange(@Param("doctorId") int doctorId, @Param("date") LocalDate date, @Param("timeStart") LocalTime timeStart, @Param("timeEnd") LocalTime timeEnd);

    @Insert("INSERT INTO room_occupation (date, time_start, time_end, doctor_id, room_id) VALUES (#{date}, #{timeStart}, #{timeEnd}, #{doctorId}, #{roomId})")
    void setRoomOccupation(@Param("date") LocalDate date, @Param("timeStart") LocalTime timeStart, @Param("timeEnd") LocalTime timeEnd, @Param("doctorId") int doctorId, @Param("roomId") int roomId);

    @Delete("DELETE FROM date_schedule WHERE doctor_id = #{doctorId} AND date = #{date}")
    void deleteSchedule(@Param("doctorId") int doctorId, @Param("date") LocalDate date);

    @Delete("DELETE FROM date_schedule WHERE doctor_id = #{doctorId} AND date >= #{date}")
    void deleteScheduleFromDate(@Param("doctorId") int doctorId, @Param("date") LocalDate date);

    @Select("SELECT room FROM commission WHERE ticket_number = #{ticketNumber}")
    String getRoomByCommissionTicket(String ticketNumber);

    @Select("SELECT id FROM slot_schedule WHERE date_id IN (SELECT id FROM date_schedule WHERE date >= #{startDate} AND date <= #{endDate} AND doctor_id = #{doctorId}) AND ticket_number NOT LIKE 'C%'")
    List<Integer> getPatientsByDoctorDateRange(@Param("doctorId") int doctorId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Select("SELECT (TIME_TO_SEC(slot_end) - TIME_TO_SEC(slot_start))/60 FROM slot_schedule WHERE date_id IN (SELECT id FROM date_schedule WHERE date >= #{startDate} AND date <= #{endDate} AND doctor_id = #{doctorId})")
    List<Integer> getWorkingMinutesByDoctorBySchedule(@Param("doctorId") int doctorId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Select("SELECT date FROM room_occupation WHERE doctor_id = #{doctorId} AND room_id = #{roomId}")
    List<LocalDate> getRoomOccupationDatesByDoctorId(@Param("doctorId") int doctorId, @Param("roomId") int roomId);
}
