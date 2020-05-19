package net.thumbtack.school.hospital.dto.request.regdoctor;

import net.thumbtack.school.hospital.dto.request.RegUserDtoRequest;
import net.thumbtack.school.hospital.validation.Date;
import net.thumbtack.school.hospital.validation.OneSchedule;

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

    //TODO validation
    private WeekSchedule weekSchedule;
    private List<WeekDaysSchedule> weekDaysSchedules = new ArrayList<>();

    @Min(value = 5, message = "Duration is too short")
    private int duration;



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

//    public RegDocDtoRequest(String regDocJson) {
//        parse(regDocJson);
//    }

//    public void parse(String regDocJson) {
//        JsonObject object = new JsonParser().parse(regDocJson).getAsJsonObject();
//        firstName = object.get("firstName").getAsString();
//        lastName = object.get("lastName").getAsString();
//        patronymic = object.get("patronymic").getAsString();
//        speciality = object.get("speciality").getAsString();
//        room = object.get("room").getAsString();
//        login = object.get("login").getAsString();
//        password = object.get("password").getAsString();
//        dateStart = object.get("dateStart").getAsString();
//        dateEnd = object.get("dateEnd").getAsString();
//
//        if (object.has("weekSchedule")
//                && object.get("weekSchedule").getAsJsonObject().size() != 0) {
////            hasWeekSchedule = true;
//            JsonObject weekSchedule = object.get("weekSchedule").getAsJsonObject();
//            this.weekSchedule.setTimeStart(weekSchedule.get("timeStart").getAsString());
//            this.weekSchedule.setTimeEnd(weekSchedule.get("timeEnd").getAsString());
//            JsonArray array = weekSchedule.get("weekDays").getAsJsonArray();
//            for (JsonElement e : array) {
//                this.weekSchedule.getWeekDays().add(e.getAsString());
//            }
//        }
//
//        if (object.has("weekDaysSchedule")
//                && object.get("weekDaysSchedule").getAsJsonArray().size() != 0) {
////            hasWeekDaysSchedule = true;
//            JsonArray weekDaysSchedule = object.get("weekDaysSchedule").getAsJsonArray();
//            for (JsonElement daySchedule: weekDaysSchedule) {
//                JsonObject obj = daySchedule.getAsJsonObject();
//                WeekDaysSchedule weekDaysSchedule1 = new WeekDaysSchedule();
//                weekDaysSchedule1.setWeekDay(obj.get("weekDay").getAsString());
//                weekDaysSchedule1.setTimeStart(obj.get("timeStart").getAsString());
//                weekDaysSchedule1.setTimeEnd(obj.get("timeEnd").getAsString());
//                weekDaysSchedules.add(weekDaysSchedule1);
//            }
//        }
//        duration = object.get("duration").getAsInt();
//
//    }

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
