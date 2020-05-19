package net.thumbtack.school.hospital.dto.response.regdoctor;

import net.thumbtack.school.hospital.dto.response.RegPatientDtoResponse;

public class SlotDtoResponse {
    private String time;
    private RegPatientDtoResponse patient;

    public SlotDtoResponse() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public RegPatientDtoResponse getPatient() {
        return patient;
    }

    public void setPatient(RegPatientDtoResponse patient) {
        this.patient = patient;
    }
}
