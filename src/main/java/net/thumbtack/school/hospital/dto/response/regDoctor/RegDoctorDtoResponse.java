package net.thumbtack.school.hospital.dto.response.regDoctor;

import java.util.ArrayList;
import java.util.List;

public class RegDoctorDtoResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String speciality;
    private String room;
    List<DayScheduleDtoResponse> dayScheduleDtoResponse = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
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

    public List<DayScheduleDtoResponse> getDayScheduleDtoResponse() {
        return dayScheduleDtoResponse;
    }

    public void setDayScheduleDtoResponse(List<DayScheduleDtoResponse> dayScheduleDtoResponse) {
        this.dayScheduleDtoResponse = dayScheduleDtoResponse;
    }
}
