package net.thumbtack.school.hospital.dto.response.regDoctor;

import java.util.ArrayList;
import java.util.List;

public class DayScheduleDtoResponse {
    private String date;
    List<SlotForDtoResponse> daySchedule = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<SlotForDtoResponse> getDaySchedule() {
        return daySchedule;
    }

    public void setSlots(List<SlotForDtoResponse> daySchedule) {
        this.daySchedule = daySchedule;
    }
}
