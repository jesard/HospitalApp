package net.thumbtack.school.hospital.error;

public enum Field {
    COOKIE("cookie"),
    LOGIN("login"),
    PASSWORD("password"),
    DATE("date"),
    DATETIME("date/time"),
    ROOM("room"),
    SCHEDULE("Schedule"),
    DOCTOR_ID("doctor id"),
    SPECIALITY("speciality"),
    TICKET_NUMBER("ticket number"),
    PATIENT_ID("patient id"),
    UNKNOWN("");

    private String message;

    Field(String message) {
        this.message = message;
    }

    public String getFieldString() {
        return message;
    }

}
