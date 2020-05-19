package net.thumbtack.school.hospital.database.mybatis.mappers;

import net.thumbtack.school.hospital.model.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorMapper {

    @Select("SELECT id FROM speciality WHERE title=#{speciality}")
    int getSpecialityId(String speciality);

    @Select("SELECT id FROM room WHERE name=#{room}")
    int getRoomId(String room);

    @Select("SELECT title FROM speciality WHERE id=#{specialityId}")
    String getSpecialityById(int specialityId);

    @Select("SELECT name FROM room WHERE id=#{roomId}")
    String getRoomById(int roomId);

    @Select("SELECT user_id as userId FROM doctor WHERE id = #{doctorId}")
    int getUserIdByDoctorId(int doctorId);

    @Insert("INSERT INTO doctor (speciality_id, room_id, user_id) VALUES (#{speciality}, #{room}, #{doctor.userId})")
    @Options(useGeneratedKeys = true, keyProperty = "doctor.id")
    void insert(@Param("doctor") Doctor doctor, @Param("speciality") int specialityId, @Param("room") int roomId);

    @Select("SELECT id, user_id as userId, speciality_id, room_id  FROM doctor WHERE user_id=#{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "speciality", column = "speciality_id", javaType = String.class,
                    one = @One(select = "getSpecialityById", fetchType = FetchType.LAZY)),
            @Result(property = "room", column = "room_id", javaType = String.class,
                    one = @One(select = "getRoomById", fetchType = FetchType.LAZY)),
            @Result(property = "schedule", column = "id", javaType = List.class,
                    many = @Many(select = "getScheduleByDoctorId", fetchType = FetchType.LAZY))
    })
    Doctor getDoctorByUserId(int userId);

    @Select("SELECT id, user_id as userId, speciality_id, room_id FROM doctor WHERE id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "speciality", column = "speciality_id", javaType = String.class,
                    one = @One(select = "getSpecialityById", fetchType = FetchType.LAZY)),
            @Result(property = "room", column = "room_id", javaType = String.class,
                    one = @One(select = "getRoomById", fetchType = FetchType.LAZY)),
            @Result(property = "schedule", column = "id", javaType = List.class,
                    many = @Many(select = "getScheduleByDoctorId", fetchType = FetchType.LAZY))
    })
    Doctor getDoctorByDoctorId(int id);

    @Select("SELECT id, user_id as userId, speciality_id, room_id FROM doctor WHERE id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "speciality", column = "speciality_id", javaType = String.class,
                    one = @One(select = "getSpecialityById", fetchType = FetchType.LAZY)),
            @Result(property = "room", column = "room_id", javaType = String.class,
                    one = @One(select = "getRoomById", fetchType = FetchType.LAZY))
    })
    Doctor getDoctorWithoutScheduleByDoctorId(int doctorId);

    @Select("SELECT id, date, doctor_id FROM date_schedule WHERE doctor_id = #{doctorId} ORDER BY date")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "date", column = "date"),
            @Result(property = "slotSchedule", column = "id", javaType = List.class,
                    many = @Many(select = "getSlotsByDateId", fetchType = FetchType.LAZY)),
            @Result(property = "doctor", column = "doctor_id", javaType = Doctor.class,
                    one = @One(select = "getDoctorByDoctorId", fetchType = FetchType.LAZY))
    })
    List<DaySchedule> getScheduleByDoctorId(int doctorId);

    @Select("SELECT id, slot_start as timeStart, slot_end as timeEnd, date_id, patient_id FROM slot_schedule WHERE date_id = #{dateId}")
    @Results({
            @Result(property = "patient", column = "patient_id", javaType = Patient.class, one =
            @One(select = "net.thumbtack.school.hospital.database.mybatis.mappers.PatientMapper.getPatientByPatientId", fetchType = FetchType.LAZY)),
            @Result(property = "daySchedule", column = "date_id", javaType = DaySchedule.class,
                    one = @One(select = "getDayScheduleById", fetchType = FetchType.LAZY))
    })
    List<Slot> getSlotsByDateId(int dateId);

    @Select("SELECT id, date, doctor_id FROM date_schedule WHERE id = #{dateId} ORDER BY date")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "date", column = "date"),
            @Result(property = "slotSchedule", column = "id", javaType = List.class,
                    many = @Many(select = "getSlotsByDateId", fetchType = FetchType.LAZY)),
            @Result(property = "doctor", column = "doctor_id", javaType = Doctor.class,
                    one = @One(select = "getDoctorByDoctorId", fetchType = FetchType.LAZY))
    })
    DaySchedule getDayScheduleById(int dateId);

    @Select("SELECT COUNT(doctor_id) FROM room_occupation WHERE room_id = #{roomId} AND date = #{date} AND time_start <= #{timeEnd} AND time_end >= #{timeStart}")
    int getDoctorIdCountByRoomDateTime(@Param("date") LocalDate date, @Param("timeStart") LocalTime timeStart, @Param("timeEnd") LocalTime timeEnd, @Param("roomId") int roomId);

    @Select("SELECT doctor_id FROM room_occupation WHERE room_id = #{roomId} AND date = #{date} AND time_start <= #{timeEnd} AND time_end >= #{timeStart}")
    int getDoctorIdByRoomDateTime(@Param("date") LocalDate date, @Param("timeStart") LocalTime timeStart, @Param("timeEnd") LocalTime timeEnd, @Param("roomId") int roomId);

    @Select("SELECT id FROM doctor")
    List<Integer> getAllDoctorIds();

    @Select("SELECT id FROM doctor WHERE speciality_id = #{specialityId}")
    List<Integer> getAllDoctorIdsBySpeciality(int specialityId);

    @Delete("DELETE FROM room_occupation WHERE doctor_id = #{doctorId} AND date = #{date}")
    void deleteRoomOccupation(@Param("doctorId") int doctorId, @Param("date") LocalDate date);

    @Delete("DELETE FROM room_occupation WHERE doctor_id = #{doctorId} AND date >= #{date}")
    void deleteRoomOccupationFromDate(@Param("doctorId") int doctorId, @Param("date") LocalDate date);

    @Select("SELECT doctor_id FROM date_schedule WHERE id = (SELECT date_id FROM slot_schedule WHERE ticket_number = #{ticketNumber})")
    int getDoctorIdByTicketNumber(String ticketNumber);

    @Select("SELECT room_id FROM doctor WHERE id = #{doctorId}")
    int getRoomIdByDoctorId(int doctorId);

    @Insert("INSERT INTO commission (time_start, time_end, room, ticket_number, patient_id) VALUES (#{timeStart}, #{timeEnd}, #{room}, #{ticketNumber}, #{patientId})")
    void insertCommission(@Param("timeStart") LocalTime timeStart, @Param("timeEnd") LocalTime timeEnd, @Param("room") String room, @Param("ticketNumber") String ticketNumber, @Param("patientId") int patientId);

    @Delete("DELETE FROM commission WHERE ticket_number = #{ticketNumber}")
    void deleteCommission(String ticketNumber);


}
