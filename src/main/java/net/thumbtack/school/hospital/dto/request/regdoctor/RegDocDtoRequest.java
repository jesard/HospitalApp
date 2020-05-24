package net.thumbtack.school.hospital.dto.request.regdoctor;

import net.thumbtack.school.hospital.dto.request.RegUserDtoRequest;
import net.thumbtack.school.hospital.validation.Date;
import net.thumbtack.school.hospital.validation.OneSchedule;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@OneSchedule(fields = {"weekSchedule", "weekDaysSchedules"})
public class RegDocDtoRequest extends RegUserDtoRequest {

    @NotEmpty(message = "Empty speciality")
    private String speciality;

    @NotEmpty(message = "Empty room")
    private String room;

    @Date
    private String dateStart;

    @Date
    private String dateEnd;

    @Valid
    private WeekSchedule weekSchedule;

    @Valid
    private List<WeekDaysSchedule> weekDaysSchedules = new ArrayList<>();

    @Min(value = 5, message = "Duration is too short")
    private int duration;

    public RegDocDtoRequest(String dateStart, String dateEnd, WeekSchedule weekSchedule, int duration) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.weekSchedule = weekSchedule;
        this.duration = duration;
    }

    public RegDocDtoRequest(String dateStart, String dateEnd, List<WeekDaysSchedule> weekDaysSchedules, int duration) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.weekDaysSchedules = weekDaysSchedules;
        this.duration = duration;
    }

    public RegDocDtoRequest(String firstName, String lastName, String patronymic, String login, String password, String speciality, String room, String dateStart, String dateEnd, int duration) {
        super(firstName, lastName, patronymic, login, password);
        this.speciality = speciality;
        this.room = room;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.duration = duration;
    }


    public RegDocDtoRequest() {
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


    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public WeekSchedule getWeekSchedule() {
        return weekSchedule;
    }

    public void setWeekSchedule(WeekSchedule weekSchedule) {
        this.weekSchedule = weekSchedule;
    }

    public List<WeekDaysSchedule> getWeekDaysSchedules() {
        return weekDaysSchedules;
    }

    public void setWeekDaysSchedules(List<WeekDaysSchedule> weekDaysSchedules) {
        this.weekDaysSchedules = weekDaysSchedules;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
