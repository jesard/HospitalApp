package net.thumbtack.school.hospital.dto.response;

public class StatsPatientDtoResponse extends RegUserDtoResponse {

    private int id;
    private String email;
    private String address;
    private String phone;
    private int appointmentsNumber;

    public StatsPatientDtoResponse(int id, String firstName, String lastName, String patronymic, String email, String address, String phone, int appointmentsNumber) {
        super(firstName, lastName, patronymic);
        this.id = id;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.appointmentsNumber = appointmentsNumber;
    }

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

    public int getAppointmentsNumber() {
        return appointmentsNumber;
    }

    public void setAppointmentsNumber(int appointmentsNumber) {
        this.appointmentsNumber = appointmentsNumber;
    }
}
