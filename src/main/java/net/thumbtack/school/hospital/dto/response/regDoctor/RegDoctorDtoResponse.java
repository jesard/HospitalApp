package net.thumbtack.school.hospital.dto.response.regDoctor;

import net.thumbtack.school.hospital.dto.response.RegUserDtoResponse;

import java.util.ArrayList;
import java.util.List;

public class RegDoctorDtoResponse extends RegUserDtoResponse {

    private int id;
    private String speciality;
    private String room;
    List<DayScheduleDtoResponse> schedule = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public List<DayScheduleDtoResponse> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<DayScheduleDtoResponse> schedule) {
        this.schedule = schedule;
    }
}
