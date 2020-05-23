package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validation.Date;
import net.thumbtack.school.hospital.validation.Time;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class RegCommissionDtoRequest {

    private int patientId;

    @NotEmpty(message = "Empty list of doctor ids")
    private List<Integer> doctorIds;

    @NotEmpty(message = "Empty room")
    private String room;

    @Date
    private String date;

    @Time
    private String time;

    @Min(value = 5, message = "Duration is too short: ${validatedValue}")
    private int duration;

    public RegCommissionDtoRequest(int patientId, List<Integer> doctorIds, String room, String date, String time, int duration) {
        this.patientId = patientId;
        this.doctorIds = doctorIds;
        this.room = room;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public RegCommissionDtoRequest() {
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public List<Integer> getDoctorIds() {
        return doctorIds;
    }

    public void setDoctorIds(List<Integer> doctorIds) {
        this.doctorIds = doctorIds;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
