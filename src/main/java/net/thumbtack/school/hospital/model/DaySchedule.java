package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class DaySchedule {

    private int id;
    private LocalDate date;
    private List<Slot> slotSchedule;
    private Doctor doctor;

    public DaySchedule(LocalDate date, List<Slot> slotSchedule) {
        this.date = date;
        this.slotSchedule = slotSchedule;
    }

    public DaySchedule() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Slot> getSlotSchedule() {
        return slotSchedule;
    }

    public void setSlotSchedule(List<Slot> slotSchedule) {
        this.slotSchedule = slotSchedule;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DaySchedule{" +
                "id=" + id +
                ", date=" + date +
                ", doctor=" + doctor.getLastName() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DaySchedule)) return false;
        DaySchedule that = (DaySchedule) o;
        return Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getSlotSchedule(), that.getSlotSchedule()) &&
                Objects.equals(getDoctor(), that.getDoctor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getSlotSchedule(), getDoctor());
    }
}
