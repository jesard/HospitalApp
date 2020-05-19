package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validation.Date;

public class DeleteDoctorDtoRequest {

    @Date
    private String date;

    public DeleteDoctorDtoRequest(String date) {
        this.date = date;
    }

    public DeleteDoctorDtoRequest() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
