// REVU net.thumbtack.school.hospital.service;
package net.thumbtack.school.hospital.Service;


import com.google.gson.Gson;
import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.dto.request.RegAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.RegPatientDtoResponse;
import net.thumbtack.school.hospital.dto.response.RegAdminDtoResponse;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.Patient;

public class AdminService {
    private AdminDao adminDao = new AdminDaoImpl();
    private Gson gson = new Gson();

    private Admin makeAdminFromDtoRequest(RegAdminDtoRequest request) {
        Admin admin = new Admin();
        UserService.makeUserFromDtoRequest(admin, request);
        admin.setPosition(request.getPosition());
        return admin;
    }

    private RegAdminDtoResponse makeDtoResponseFromAdmin(Admin admin) {
        RegAdminDtoResponse response = new RegAdminDtoResponse();
        UserService.makeDtoResponseFromUser(response, admin);
        response.setId(admin.getId());
        response.setPosition(admin.getPosition());
        return response;
    }

    public String registerAdmin(String registerAdminJson) {
        RegAdminDtoRequest regAdminDtoRequest = gson.fromJson(registerAdminJson, RegAdminDtoRequest.class);
        Admin admin = makeAdminFromDtoRequest(regAdminDtoRequest);
        adminDao.insertAdmin(admin);
        RegAdminDtoResponse response = makeDtoResponseFromAdmin(admin);
        return gson.toJson(response);
    }
}

