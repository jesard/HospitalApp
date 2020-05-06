package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.User;

public interface UserDao {

    void insertUser(User user, String descriptor);

    User getUser(String login);

    User getUserById(int id);

    void deleteUser(User user);

    void updateUser(User newUser);

    void login(int userId, String token);

    int getUserIdByToken(String token);

    String getDescriptorByUserId(int userId);
}
