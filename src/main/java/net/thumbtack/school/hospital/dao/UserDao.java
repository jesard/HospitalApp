package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.User;

public interface UserDao {

    void insertUser(User user, String descriptor) throws ServerException;

    User getUserByLogin(String login) throws ServerException;

    User getUserById(int id) throws ServerException;

    void deleteUser(User user);

    void updateUser(User newUser);

    void login(int userId, String token);

    int getUserIdByToken(String token) throws ServerException;

    String getDescriptorByUserId(int userId) throws ServerException;

    void logout(String token);
}
