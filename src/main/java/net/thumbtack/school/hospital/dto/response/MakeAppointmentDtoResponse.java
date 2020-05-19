package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.dto.response.regdoctor.RegDoctorDtoResponse;

public class MakeAppointmentDtoResponse extends RegDoctorDtoResponse {

    private String ticket;
    private String date;
    private String time;

    public MakeAppointmentDtoResponse(String ticket, int doctorId, String firstName, String lastName, String patronymic, String speciality, String room, String date, String time) {
        super(doctorId, firstName, lastName, patronymic, speciality, room);
        this.ticket = ticket;
        this.date = date;
        this.time = time;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
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
