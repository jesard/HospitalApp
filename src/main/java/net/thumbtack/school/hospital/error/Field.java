package net.thumbtack.school.hospital.error;

public enum Field {
    COOKIE("cookie"),
    FIRSTNAME("firstName"),
    LASTNAME("lastName"),
    LOGIN("login"),
    PASSWORD("password"),
    DATE("date"),
    TIME("time"),
    DATETIME("date/time"),
    ROOM("room"),
    SCHEDULE("Schedule"),
    DOCTORID("doctor id"),
    SPECIALITY("speciality"),
    UNKNOWN("");

    private String message;

    Field(String message) {
        this.message = message;
    }

    public String getFieldString() {
        return message;
    }

}
