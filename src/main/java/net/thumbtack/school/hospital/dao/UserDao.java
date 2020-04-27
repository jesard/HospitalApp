package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.User;

public interface UserDao {

    void insertUser(User user, String descriptor);

    User getUser(String login);

    void deleteUser(User user);

    void addSession(String token, User user);

    User getSession(String token);

    void removeSession(String token);


}
