package net.thumbtack.school.hospital.dto.request;

public class RegPatientDtoRequest extends RegUserDtoRequest {

    private String email;
    private String address;
    private String phone;

    public RegPatientDtoRequest(String firstName, String lastName, String patronymic, String email, String address, String phone, String login, String password) {
        super(firstName, lastName, patronymic, login, password);
        this.email = email;
        this.address = address;
        this.phone = phone;
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
