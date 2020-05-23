package net.thumbtack.school.hospital.dto.request.regdoctor;

import net.thumbtack.school.hospital.validation.Time;
import net.thumbtack.school.hospital.validation.WeekDay;
import net.thumbtack.school.hospital.validation.WeekDays;

import java.util.ArrayList;
import java.util.List;

public class WeekSchedule {

    @Time
    private String timeStart;

    @Time
    private String timeEnd;

    @WeekDays
    private List<String> weekDays = new ArrayList<>();

    public WeekSchedule(String timeStart, String timeEnd, List<String> weekDays) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.weekDays = weekDays;
    }

    public WeekSchedule() {
    }

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
