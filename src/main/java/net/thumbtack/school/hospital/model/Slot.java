package net.thumbtack.school.hospital.model;

import java.time.LocalTime;
import java.util.Objects;

public class Slot {

    private int id;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private DaySchedule daySchedule;
    private String ticketNumber;
    private Patient patient;

    public Slot(LocalTime timeStart, LocalTime timeEnd) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public Slot() {
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public DaySchedule getDaySchedule() {
        return daySchedule;
    }

    public void setDaySchedule(DaySchedule daySchedule) {
        this.daySchedule = daySchedule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "id=" + id +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", date=" + daySchedule.getDate() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Slot)) return false;
        Slot slot = (Slot) o;
        return Objects.equals(getTimeStart(), slot.getTimeStart()) &&
                Objects.equals(getTimeEnd(), slot.getTimeEnd()) &&
                Objects.equals(getDaySchedule(), slot.getDaySchedule()) &&
                Objects.equals(getPatient(), slot.getPatient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTimeStart(), getTimeEnd(), getDaySchedule(), getPatient());
    }
}
