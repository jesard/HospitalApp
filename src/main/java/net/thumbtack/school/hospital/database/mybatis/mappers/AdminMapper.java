package net.thumbtack.school.hospital.database.mybatis.mappers;

import net.thumbtack.school.hospital.model.Admin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface AdminMapper {

    @Insert("INSERT INTO admin (position, user_id) VALUES (#{position}, #{userId})")
    @Options(useGeneratedKeys = true)
    public void insert(Admin admin);

    @Select("SELECT id, position, user_id as userId FROM admin WHERE user_id = #{userId}")
    Admin getAdminByUserId(int userId);

    @Update("UPDATE admin SET position = #{position} WHERE id = #{id}")
    void update(Admin newAdmin);
}
