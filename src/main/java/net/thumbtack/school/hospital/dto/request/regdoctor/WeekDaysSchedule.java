package net.thumbtack.school.hospital.dto.request.regdoctor;

import net.thumbtack.school.hospital.validation.Time;
import net.thumbtack.school.hospital.validation.WeekDay;

public class WeekDaysSchedule {

    @WeekDay
    private String weekDay;

    @Time
    private String timeStart;

    @Time
    private String timeEnd;

    public WeekDaysSchedule(String weekDay, String timeStart, String timeEnd) {
        this.weekDay = weekDay;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public WeekDaysSchedule() {
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
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
}
