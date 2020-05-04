package net.thumbtack.school.hospital.dto.request.regDoctor;

import java.util.ArrayList;
import java.util.List;

public class WeekSchedule {
    private String timeStart;
    private String timeEnd;
    List<String> weekDays = new ArrayList<>();

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public List<String> getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(List<String> weekDays) {
        this.weekDays = weekDays;
    }
}
