package net.thumbtack.school.hospital.database.mybatis.mappers;

import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.annotations.*;

public interface UserMapper {

    @Insert("INSERT INTO user (firstName, lastName, patronymic, login, password, descriptor) VALUES (#{user.firstName}, #{user.lastName}, #{user.patronymic}, #{user.login}, #{user.password}, #{descriptor})")
    @Options(useGeneratedKeys = true, keyProperty = "user.userId")
    void insert(@Param("user") User user, @Param("descriptor") String descriptor);

    @Select("SELECT id as userId, firstName, patronymic, lastName, login, password FROM user WHERE login = #{login}")
    User getUser(String login);

    @Select("SELECT id as userId, firstName, patronymic, lastName, login, password FROM user WHERE id = #{userId}")
    User getById(int userId);

    @Delete("DELETE FROM user WHERE (login <> 'admin')")
    void deleteAllUsers();

    @Delete("DELETE FROM session")
    void deleteAllSessions();

    @Delete("DELETE FROM user WHERE id = #{userId}")
    void delete(User user);

    @Update("UPDATE user SET firstName = #{firstName}, lastName = #{lastName}, patronymic = #{patronymic}, password = #{password} WHERE id = #{userId}")
    void update(User newUser);

    @Insert("INSERT INTO session (user_id, token) VALUES (#{userId}, #{token})")
    void login(@Param("userId") int userId, @Param("token") String token);

    @Delete("DELETE FROM session WHERE token = #{token}")
    void logout(String token);

    @Select("SELECT user_id FROM session WHERE token = #{token}")
    int getUserIdByToken(String token);

    @Select("SELECT descriptor FROM user WHERE id = #{userId}")
    String getDescriptorByUserId(int userId);
}
