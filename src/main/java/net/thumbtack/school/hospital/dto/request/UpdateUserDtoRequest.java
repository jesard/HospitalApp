package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validation.MaxNameLength;
import net.thumbtack.school.hospital.validation.PasswordMinLength;
import net.thumbtack.school.hospital.validation.RussianName;

public class UpdateUserDtoRequest {

    @RussianName
    @MaxNameLength
    private String firstName;

    @RussianName
    @MaxNameLength
    private String lastName;

    @RussianName
    @MaxNameLength
    private String patronymic;

    private String oldPassword;

    @MaxNameLength
    @PasswordMinLength
    private String newPassword;

    public UpdateUserDtoRequest(String firstName, String lastName, String patronymic, String oldPassword, String newPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public UpdateUserDtoRequest() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
