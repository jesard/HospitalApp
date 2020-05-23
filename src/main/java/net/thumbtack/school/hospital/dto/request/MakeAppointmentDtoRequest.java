package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validation.Date;
import net.thumbtack.school.hospital.validation.OneDoctor;
import net.thumbtack.school.hospital.validation.Time;

@OneDoctor
public class MakeAppointmentDtoRequest {

    private int doctorId;
    private String speciality;

    @Date
    private String date;

    @Time
    private String time;

    public MakeAppointmentDtoRequest(int doctorId, String speciality, String date, String time) {
        this.doctorId = doctorId;
        this.speciality = speciality;
        this.date = date;
        this.time = time;
    }

    public MakeAppointmentDtoRequest() {
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
