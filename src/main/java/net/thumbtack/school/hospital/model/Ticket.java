package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Ticket {

    private int doctorId;
    private String room;
    private LocalDate date;
    private LocalTime time;

    public Ticket(int doctorId, String room, LocalDate date, LocalTime time) {
        this.doctorId = doctorId;
        this.room = room;
        this.date = date;
        this.time = time;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
