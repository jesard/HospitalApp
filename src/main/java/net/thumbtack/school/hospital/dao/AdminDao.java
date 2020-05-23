package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.Admin;

public interface AdminDao extends UserDao {

    Admin insertAdmin(Admin admin) throws ServerException;

//    Admin getAdminByLogin(String login);

    Admin getAdminByUserId(int id) throws ServerException;

    void updateAdmin(Admin newAdmin);
}
