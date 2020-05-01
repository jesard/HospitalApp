// REVU net.thumbtack.school.hospital.service;
package net.thumbtack.school.hospital.Service;


import com.google.gson.Gson;
import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.RegisterAdminDtoResponse;
import net.thumbtack.school.hospital.model.Admin;

public class AdminService {
	// REVU DAO не надо static
    private static AdminDao adminDao = new AdminDaoImpl();
    private static Gson gson = new Gson();

    // REVU не надо static.
    public static String registerAdmin(String registerAdminJson) {
        RegisterAdminDtoRequest registerAdminDtoRequest = gson.fromJson(registerAdminJson, RegisterAdminDtoRequest.class);
        String firstName = registerAdminDtoRequest.getFirstName();
        String lastName = registerAdminDtoRequest.getLastName();
        String patronymic = registerAdminDtoRequest.getPatronymic();
        String position = registerAdminDtoRequest.getPosition();
        String login = registerAdminDtoRequest.getLogin();
        String password = registerAdminDtoRequest.getPassword();
        Admin admin = new Admin(firstName, lastName, login, password, position);
        admin.setPatronymic(patronymic);
        return gson.toJson(new RegisterAdminDtoResponse(adminDao.insertAdmin(admin)));
    }
}

