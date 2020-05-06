package net.thumbtack.school.hospital.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Doctor extends User {

    private int id;
    private String speciality;
    private String room;
    private List<DaySchedule> schedule = new ArrayList<>();

    public Doctor(String firstName, String lastName, String login, String password, String speciality, String room) {
        super(firstName, lastName, login, password);
        this.speciality = speciality;
        this.room = room;
    }

    public Doctor(User user, String speciality, String room) {
        super(user.getFirstName(), user.getLastName(), user.getLogin(), user.getPassword());
        this.speciality = speciality;
        this.room = room;
    }

    public Doctor() {}

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public List<DaySchedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<DaySchedule> schedule) {
        this.schedule = schedule;
    }

    public int getId() {
        return id;
    }

    public void setId(int userId) {
        this.id = userId;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", speciality='" + speciality + '\'' +
                ", room='" + room + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        Doctor doctor = (Doctor) o;
        return  Objects.equals(getSpeciality(), doctor.getSpeciality()) &&
                Objects.equals(getRoom(), doctor.getRoom()) &&
                Objects.equals(getSchedule(), doctor.getSchedule());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSpeciality(), getRoom(), getSchedule());
    }
}
