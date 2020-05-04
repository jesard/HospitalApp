package net.thumbtack.school.hospital.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Patient extends User {

    private int id;
    private String email;
    private String address;
    private String phone;
    private List<Slot> tickets = new ArrayList<>();

    public Patient(String firstName, String lastName, String login, String password, String email, String address, String phone) {
        super(firstName, lastName, login, password);
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public Patient() {}

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Slot> getTickets() {
        return tickets;
    }

    public void setTickets(List<Slot> tickets) {
        this.tickets = tickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return  Objects.equals(getEmail(), patient.getEmail()) &&
                Objects.equals(getAddress(), patient.getAddress()) &&
                Objects.equals(getPhone(), patient.getPhone()) &&
                Objects.equals(getTickets(), patient.getTickets());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getAddress(), getPhone(), getTickets());
    }
}
