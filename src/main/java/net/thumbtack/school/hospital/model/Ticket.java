package net.thumbtack.school.hospital.model;

public class Ticket {

    private Doctor doctor;
    private Slot slot;

    public Ticket(Doctor doctor, Slot slot) {
        this.doctor = doctor;
        this.slot = slot;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }
}
