package net.thumbtack.school.hospital.service;


import net.thumbtack.school.hospital.dto.request.RegAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminDtoRequest;
import net.thumbtack.school.hospital.error.Field;
import net.thumbtack.school.hospital.error.MyError;
import net.thumbtack.school.hospital.dto.response.RegAdminDtoResponse;
import net.thumbtack.school.hospital.error.ServerErrorCode;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.Admin;

public class AdminService extends UserService {

    private Admin makeAdminFromDtoRequest(RegAdminDtoRequest request) {
        Admin admin = new Admin();
        makeUserFromDtoRequest(admin, request);
        admin.setPosition(request.getPosition());
        return admin;
    }

    public RegAdminDtoResponse registerAdmin(RegAdminDtoRequest request, String token) throws ServerException {
        if (getUserDecriptorByToken(token).equals(ADMIN)) {
            Admin admin = makeAdminFromDtoRequest(request);
            adminDao.insertAdmin(admin);
            return new RegAdminDtoResponse(admin.getId(), admin.getFirstName(), admin.getFirstName(), admin.getPatronymic(), admin.getPosition());
        } else {
            throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
        }
    }

    public RegAdminDtoResponse updateAdmin(UpdateAdminDtoRequest request, String token) throws ServerException {
        if (getUserDecriptorByToken(token).equals(ADMIN)) {
            int userId = userDao.getUserIdByToken(token);
            Admin admin = adminDao.getAdminByUserId(userId);
            if (admin.getPassword().equals(request.getOldPassword())) {
                Admin newAdmin = new Admin(request.getFirstName(), request.getLastName(), request.getPatronymic(), admin.getLogin(), request.getNewPassword(), request.getPosition());
                newAdmin.setUserId(userId);
                newAdmin.setId(admin.getId());
                adminDao.updateAdmin(newAdmin);
                return (new RegAdminDtoResponse(newAdmin.getId(), newAdmin.getFirstName(), newAdmin.getLastName(), newAdmin.getPatronymic(), newAdmin.getPosition()));
            }
            throw new ServerException(new MyError(ServerErrorCode.WRONG_PASSWORD, Field.PASSWORD));
        } else {
            throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
        }
    }


}

