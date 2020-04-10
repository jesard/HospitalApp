package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Doctor extends User {

    private String speciality;
    private String room;
    // REVU можно, конечно, и Map, даже логично
    // но можно и просто List<DaySchedule>
    // в нем же дата есть, а поиск , хоть и линейный, тут не будет медленным, так как элементов очень мало
    // на Ваше усмотрение
    // если оставите Map - надо будет разобраться, как его с помощью DAO получить
    private Map<LocalDate, DaySchedule> schedule = new HashMap<>();

    public Doctor(String firstName, String lastName, String login, String password, String speciality, String room) {
        super(firstName, lastName, login, password);
        this.speciality = speciality;
        this.room = room;
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
}
