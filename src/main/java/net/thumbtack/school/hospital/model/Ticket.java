package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;

// REVU пока неясно, куда этот класс относится, где будет на него ссылка
// и нужен ли он в таком виде вообще 
// тикет по идее (если не брать комиссии во внимание) привязывается к приему, то есть к Slot
// а в Slot время и так есть
public class Ticket {

	// REVU вместо этого должно быть private Doctor doctor;
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
