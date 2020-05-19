package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.User;

public interface UserDao {

    void insertUser(User user, String descriptor);

    User getUser(String login);

    User getUserById(int id);

    void deleteUser(User user);

    void updateUser(User newUser);

    void login(int userId, String token);

    int getUserIdByToken(String token) throws ServerException;

    String getDescriptorByUserId(int userId);

    void logout(String token);
}
