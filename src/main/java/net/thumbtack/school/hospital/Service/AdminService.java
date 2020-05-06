// REVU net.thumbtack.school.hospital.service;
package net.thumbtack.school.hospital.Service;


import net.thumbtack.school.hospital.dto.request.RegAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.RegAdminDtoResponse;
import net.thumbtack.school.hospital.model.Admin;

public class AdminService extends UserService {

    protected static Admin makeAdminFromDtoRequest(RegAdminDtoRequest request) {
        Admin admin = new Admin();
        UserService.makeUserFromDtoRequest(admin, request);
        admin.setPosition(request.getPosition());
        return admin;
    }

    protected static RegAdminDtoResponse makeDtoResponseFromAdmin(Admin admin) {
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

    public String updateDoctorSchedule(String updateDoctorScheduleJson) {
        return null;
    }

    public String deleteDoctor(String deleteDoctorJson) {
        return null;
    }

}

