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

    @Select("SELECT id, email, address, phone FROM patient WHERE id = #{patientId}")
    @Results({
            @Result(property = "tickets", column = "id", javaType = List.class,
                    many = @Many(select = "getSlotsByPatientId", fetchType = FetchType.LAZY)),
    })
    Patient getPatientByPatientId(int patientId);

    @Select("SELECT id, email, address, phone, user_id as userId FROM patient WHERE user_id = #{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "tickets", column = "id", javaType = List.class,
                    many = @Many(select = "getSlotsByPatientId", fetchType = FetchType.LAZY)),
    })
    Patient getPatientByUserId(int userId);

    @Select("SELECT id, slot_start as timeStart, slot_end as timeEnd, ticket_number as ticketNumber FROM slot_schedule WHERE patient_id = #{patientId}")
    @Results({
            @Result(property = "daySchedule", column = "date_id", javaType = DaySchedule.class, many =
            @Many(select = "net.thumbtack.school.hospital.database.mybatis.mappers.DoctorMapper.getSlotsByDateId", fetchType = FetchType.LAZY))
    })
    List<Slot> getSlotsByPatientId(int patientId);

    @Insert("INSERT INTO patient (email, address, phone, user_id) VALUES (#{email}, #{address}, #{phone}, #{userId})")
    @Options(useGeneratedKeys = true)
    void insert(Patient patient);

    @Update("UPDATE slot_schedule SET ticket_number = #{ticketNumber}, patient_id = #{patient.id} WHERE id = #{slotId}")
    void makeAppointment(@Param("patient") Patient patient, @Param("slotId") int slotId, @Param("ticketNumber") String ticketNumber);
}
