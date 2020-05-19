package net.thumbtack.school.hospital.dto.request;

import javax.validation.constraints.NotEmpty;

public class UpdateAdminDtoRequest extends UpdateUserDtoRequest {

    @NotEmpty
    private String position;

    public UpdateAdminDtoRequest(String firstName, String lastName, String patronymic, String oldPassword, String newPassword, String position) {
        super(firstName, lastName, patronymic, oldPassword, newPassword);
        this.position = position;
    }

    public UpdateAdminDtoRequest() {
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
