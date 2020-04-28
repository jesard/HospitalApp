package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.Admin;

public interface AdminDao extends UserDao {

    Admin insertAdmin(Admin admin);

    Admin getAdmin(String login);
}
