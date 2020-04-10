package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Patient extends User {

    private String email;
    private String address;
    private String phone;
    // REVU см. REVU в классе Doctor
    private Map<LocalDate, List<Ticket>> tickets = new HashMap<>();

    public Patient(String firstName, String lastName, String login, String password, String email, String address, String phone) {
        super(firstName, lastName, login, password);
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
