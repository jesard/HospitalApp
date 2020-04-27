package net.thumbtack.school.hospital.database.mybatis.mappers;

import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.annotations.*;

public interface UserMapper {

    @Insert("INSERT INTO user (firstName, lastName, patronymic, login, password, descriptor) VALUES (#{user.firstName}, #{user.lastName}, #{user.patronymic}, #{user.login}, #{user.password}, #{descriptor})")
    @Options(useGeneratedKeys = true, keyProperty = "user.userId")
    void insert(@Param("user") User user, @Param("descriptor") String descriptor);

    @Select("SELECT id as userId, firstName, lastName, login, password FROM user WHERE login = #{login}")
    User get(String login);

    @Delete("DELETE FROM user WHERE (login <> 'admin')")
    void deleteAll();

    @Delete("DELETE FROM user WHERE id = #{userId}")
    void delete(User user);
}
