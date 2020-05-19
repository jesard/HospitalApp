package net.thumbtack.school.hospital.dto.response.regdoctor;

import java.util.List;

public class TicketDtoResponse {

    private String ticket;
    private String room;
    private String date;
    private String time;

    private RegDoctorDtoResponse doctor;
    private List<RegDoctorDtoResponse> doctors;

    public TicketDtoResponse(String ticket, String room, String date, String time, RegDoctorDtoResponse doctor) {
        this.ticket = ticket;
        this.room = room;
        this.date = date;
        this.time = time;
        this.doctor = doctor;
    }

    public TicketDtoResponse(String ticket, String room, String date, String time, List<RegDoctorDtoResponse> doctors) {
        this.ticket = ticket;
        this.room = room;
        this.date = date;
        this.time = time;
        this.doctors = doctors;
    }

    public TicketDtoResponse() {
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
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

    public RegDoctorDtoResponse getDoctor() {
        return doctor;
    }

    public void setDoctor(RegDoctorDtoResponse doctor) {
        this.doctor = doctor;
    }

    public List<RegDoctorDtoResponse> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<RegDoctorDtoResponse> doctors) {
        this.doctors = doctors;
    }
}
