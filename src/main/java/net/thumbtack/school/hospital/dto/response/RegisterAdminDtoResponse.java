package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.model.Admin;

public class RegisterAdminDtoResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String position;

    public RegisterAdminDtoResponse(Admin admin) {
        id = admin.getId();
        firstName = admin.getFirstName();
        lastName = admin.getLastName();
        patronymic = admin.getPatronymic();
        position = admin.getPosition();
    }

}
