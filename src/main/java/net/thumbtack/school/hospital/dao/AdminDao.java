package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.User;

public interface AdminDao extends UserDao {

    void insertAdmin(Admin admin);

    Admin getAdmin(String login);
}
