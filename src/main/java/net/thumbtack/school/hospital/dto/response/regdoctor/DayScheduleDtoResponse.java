package net.thumbtack.school.hospital.dto.response.regdoctor;

import java.util.ArrayList;
import java.util.List;

public class DayScheduleDtoResponse {
    private String date;
    private List<SlotDtoResponse> daySchedule = new ArrayList<>();

    public DayScheduleDtoResponse() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<SlotDtoResponse> getDaySchedule() {
        return daySchedule;
    }

    public void setSlots(List<SlotDtoResponse> daySchedule) {
        this.daySchedule = daySchedule;
    }
}
