package net.thumbtack.school.hospital.dto.request.regDoctor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class RegDocDtoRequestByHands {

    private String firstName;
    private String lastName;
    private String patronymic;
    private String speciality;
    private String room;
    private String login;
    private String password;
    private String dateStart;
    private String dateEnd;
    private WeekSchedule weekSchedule = new WeekSchedule();
    private List<WeekDaysSchedule> weekDaysSchedules = new ArrayList<>();
    private int duration;
    private boolean hasWeekSchedule = false;
    private boolean hasWeekDaysSchedule = false;

//    public RegDocDtoRequestByHands(String regDocJson) {
//        parse(regDocJson);
//    }

    public void parse(String regDocJson) {
        JsonObject object = new JsonParser().parse(regDocJson).getAsJsonObject();
        firstName = object.get("firstName").getAsString();
        lastName = object.get("lastName").getAsString();
        patronymic = object.get("patronymic").getAsString();
        speciality = object.get("speciality").getAsString();
        room = object.get("room").getAsString();
        login = object.get("login").getAsString();
        password = object.get("password").getAsString();
        dateStart = object.get("dateStart").getAsString();
        dateEnd = object.get("dateEnd").getAsString();

        if (object.has("weekSchedule")
                && object.get("weekSchedule").getAsJsonObject().size() != 0) {
            hasWeekSchedule = true;
            JsonObject weekSchedule = object.get("weekSchedule").getAsJsonObject();
            this.weekSchedule.setTimeStart(weekSchedule.get("timeStart").getAsString());
            this.weekSchedule.setTimeEnd(weekSchedule.get("timeEnd").getAsString());
            JsonArray array = weekSchedule.get("weekDays").getAsJsonArray();
            for (JsonElement e : array) {
                this.weekSchedule.getWeekDays().add(e.getAsString());
            }
        }

        if (object.has("weekDaysSchedule")
                && object.get("weekDaysSchedule").getAsJsonArray().size() != 0) {
            hasWeekDaysSchedule = true;
            JsonArray weekDaysSchedule = object.get("weekDaysSchedule").getAsJsonArray();
            for (JsonElement daySchedule: weekDaysSchedule) {
                JsonObject obj = daySchedule.getAsJsonObject();
                WeekDaysSchedule weekDaysSchedule1 = new WeekDaysSchedule();
                weekDaysSchedule1.setWeekDay(obj.get("weekDay").getAsString());
                weekDaysSchedule1.setTimeStart(obj.get("timeStart").getAsString());
                weekDaysSchedule1.setTimeEnd(obj.get("timeEnd").getAsString());
                weekDaysSchedules.add(weekDaysSchedule1);
            }
        }
        duration = object.get("duration").getAsInt();

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean hasWeekSchedule() {
        return hasWeekSchedule;
    }

    public void setHasWeekSchedule(boolean hasWeekSchedule) {
        this.hasWeekSchedule = hasWeekSchedule;
    }

    public boolean hasWeekDaysSchedule() {
        return hasWeekDaysSchedule;
    }

    public void setHasWeekDaysSchedule(boolean hasWeekDaysSchedule) {
        this.hasWeekDaysSchedule = hasWeekDaysSchedule;
    }
}
