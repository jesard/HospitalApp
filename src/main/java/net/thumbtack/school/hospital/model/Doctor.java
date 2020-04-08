package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Doctor extends User {

    private String speciality;
    private String room;
    private Map<LocalDate, DaySchedule> schedule = new HashMap<>();

    public Doctor(String firstName, String lastName, String login, String password, String speciality, String room) {
        super(firstName, lastName, login, password);
        this.speciality = speciality;
        this.room = room;
    }

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
}
