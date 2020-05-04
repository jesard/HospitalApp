package net.thumbtack.school.hospital.dto.response.regDoctor;

import net.thumbtack.school.hospital.model.Patient;

public class SlotForDtoResponse {
    private String time;
    private Patient patient;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
