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

public interface PatientMapper {

    @Select("SELECT user_id FROM patient WHERE id = #{patientId}")
    int getUserIdByPatientId(int patientId);

    @Select("SELECT id, email, address, phone, user_id as userId FROM patient WHERE id = #{patientId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "tickets", column = "id", javaType = List.class,
                    many = @Many(select = "getSlotsByPatientId", fetchType = FetchType.LAZY)),
    })
    Patient getPatientByPatientId(int patientId);

    @Select("SELECT id, email, address, phone FROM patient WHERE user_id = #{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "tickets", column = "id", javaType = List.class,
                    many = @Many(select = "getSlotsByPatientId", fetchType = FetchType.LAZY)),
            @Result(property = "commissions", column = "id", javaType = List.class,
                    many = @Many(select = "getCommissionsByPatientId", fetchType = FetchType.LAZY))
    })
    Patient getPatientByUserId(int userId);

    @Select("SELECT id, slot_start as timeStart, slot_end as timeEnd, date_id, ticket_number as ticketNumber FROM slot_schedule WHERE patient_id = #{patientId} AND ticket_number NOT LIKE 'C%'")
    @Results({
            @Result(property = "daySchedule", column = "date_id", javaType = DaySchedule.class, many =
            @Many(select = "net.thumbtack.school.hospital.database.mybatis.mappers.DoctorMapper.getDayScheduleById", fetchType = FetchType.LAZY))
    })
    List<Slot> getSlotsByPatientId(int patientId);

    @Select("SELECT time_start as timeStart, time_end as timeEnd, ticket_number as ticketNumber FROM commission WHERE patient_id = #{patientId}")
    List<Slot> getCommissionsByPatientId(int patientId);

    @Insert("INSERT INTO patient (email, address, phone, user_id) VALUES (#{email}, #{address}, #{phone}, #{userId})")
    @Options(useGeneratedKeys = true)
    void insert(Patient patient);

    @Update("UPDATE slot_schedule SET ticket_number = #{ticketNumber}, patient_id = #{patientId} WHERE id = #{slotId} AND ticket_number IS NULL")
    int makeAppointment(@Param("patientId") int patientId, @Param("slotId") int slotId, @Param("ticketNumber") String ticketNumber);

    @Update("UPDATE patient SET email = #{email}, address = #{address}, phone = #{phone} WHERE id = #{id}")
    void update(Patient newPatient);

    @Update("UPDATE slot_schedule SET ticket_number = NULL, patient_id = NULL WHERE ticket_number = #{ticketNumber}")
    void deleteTicket(String ticketNumber);

    @Select("SELECT patient_id FROM slot_schedule WHERE ticket_number = #{ticketNumber}")
    int getPatientIdByTicketNumber(String ticketNumber);

    @Select("SELECT COUNT(id) FROM slot_schedule WHERE patient_id = #{patientId} AND date_id IN (SELECT id FROM date_schedule WHERE date >= #{startDate} AND date <= #{endDate}) AND ticket_number NOT LIKE 'C%'")
    int getPatientAppointmentsNumber(@Param("patientId")int patientId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Select("SELECT id, slot_start as timeStart, slot_end as timeEnd, date_id, ticket_number as ticketNumber FROM slot_schedule WHERE patient_id = #{patientId} AND date_id IN (SELECT id FROM date_schedule WHERE date = #{date})")
    @Results({
            @Result(property = "daySchedule", column = "date_id", javaType = DaySchedule.class, many =
            @Many(select = "net.thumbtack.school.hospital.database.mybatis.mappers.DoctorMapper.getDayScheduleById", fetchType = FetchType.LAZY))
    })
    List<Slot> getBusySlotsByDatePatientId(@Param("patientId") int patientId, @Param("date") LocalDate date);
}
