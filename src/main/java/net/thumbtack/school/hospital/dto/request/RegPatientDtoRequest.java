package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validation.MobilePhone;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class RegPatientDtoRequest extends RegUserDtoRequest {

    @Email(message = "Invalid email ${validatedValue}")
    private String email;

    @NotEmpty(message = "Empty address")
    private String address;

    @MobilePhone
    private String phone;

    public RegPatientDtoRequest(String firstName, String lastName, String patronymic, String email, String address, String phone, String login, String password) {
        super(firstName, lastName, patronymic, login, password);
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public RegPatientDtoRequest() {super();}


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
