package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.util.List;

public class DaySchedule {

    private LocalDate date;
    private List<Slot> slotSchedule;

    public DaySchedule(LocalDate date, List<Slot> detailedSchedule) {
        this.date = date;
        this.slotSchedule = detailedSchedule;
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
}
