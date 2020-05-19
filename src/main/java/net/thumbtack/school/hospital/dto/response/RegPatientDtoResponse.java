package net.thumbtack.school.hospital.dto.response;

public class RegPatientDtoResponse extends RegUserDtoResponse {

    private int id;
    private String email;
    private String address;
    private String phone;

    public RegPatientDtoResponse(int id, String firstName, String lastName, String patronymic, String email, String address, String phone) {
        super(firstName, lastName, patronymic);
        this.id = id;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public RegPatientDtoResponse(String firstName, String lastName, String patronymic, String email, String address, String phone) {
        this(0, firstName, lastName, patronymic, email, address, phone);
    }

    public RegPatientDtoResponse() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
