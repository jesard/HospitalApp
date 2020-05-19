package net.thumbtack.school.hospital.dto.request;

import javax.validation.constraints.NotEmpty;

public class RegAdminDtoRequest extends RegUserDtoRequest {

    @NotEmpty
    private String position;

    public RegAdminDtoRequest(String firstName, String lastName, String patronymic, String position, String login, String password) {
        super(firstName, lastName, patronymic, login, password);
        this.position = position;
    }

    public RegAdminDtoRequest() {
        super();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
