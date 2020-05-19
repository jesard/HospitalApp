package net.thumbtack.school.hospital.dto.response;

import java.util.List;

public class RegCommissionDtoResponse {

    private String ticket;
    private int patientId;
    private List<Integer> doctorIds;
    private String room;
    private String date;
    private String time;
    private int duration;

    public RegCommissionDtoResponse(String ticket, int patientId, List<Integer> doctorIds, String room, String date, String time, int duration) {
        this.ticket = ticket;
        this.patientId = patientId;
        this.doctorIds = doctorIds;
        this.room = room;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public RegCommissionDtoResponse() {
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
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
