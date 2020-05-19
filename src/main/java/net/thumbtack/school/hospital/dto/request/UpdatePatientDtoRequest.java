package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validation.MobilePhone;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UpdatePatientDtoRequest extends UpdateUserDtoRequest {

    @Email
    private String email;

    @NotEmpty
    private String address;

    @MobilePhone
    private String phone;

    public UpdatePatientDtoRequest(String firstName, String lastName, String patronymic, String oldPassword, String newPassword, String email, String address, String phone) {
        super(firstName, lastName, patronymic, oldPassword, newPassword);
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public UpdatePatientDtoRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
