package net.thumbtack.school.hospital.database.mybatis.mappers;

import net.thumbtack.school.hospital.model.Admin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface AdminMapper {

    @Insert("INSERT INTO admin (position, user_id) VALUES (#{position}, #{userId})")
    @Options(useGeneratedKeys = true)
    public void insert(Admin admin);

    @Select("SELECT position FROM admin WHERE user_id = #{userId}")
    String getPosition(int userId);
}
