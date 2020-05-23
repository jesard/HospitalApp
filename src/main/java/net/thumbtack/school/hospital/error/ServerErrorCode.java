package net.thumbtack.school.hospital.error;

public enum ServerErrorCode {
    USER_DUPLICATE("User duplicate %s"),
    USER_NOT_FOUND("User not found %s"),
    WRONG_LOGIN_OR_PASSWORD("Wrong login or password %s"),
    WRONG_PASSWORD("Wrong password"),
    INVALID_LOGIN("Invalid login %s"),
    INVALID_RUSSIAN_NAME("Invalid russian name %s"),
    MAX_NAME_LENGTH("Max name length exceeded %s"),
    PASSWORD_MIN_LENGTH("Too short password"),
    ROOM_IS_BUSY("Room is busy %s"),
    ROOM_NOT_FOUND("Room not found %s"),
    WRONG_SCHEDULE("Can't replace schedule - old schedule have appointments"),
    WRONG_USER("User have no required permissions"),
    DOCTOR_IS_BUSY("Doctor is busy %s"),
    SLOT_IS_BUSY("The time is not free %s"),
    WRONG_ROOM("Wrong room %s"),
    WRONG_DATE("Wrong date %s"),
    WRONG_TIME("Wrong time %s"),
    WRONG_DAY_OF_WEEK("Wrong day of week %s"),
    SPECIALITY_NOT_FOUND("Speciality not found %s"),
    TICKET_NOT_FOUND("Ticket not found %s"),
    ONE_NOT_NULL("One value should be empty"),
    WRONG_MOBILE_PHONE("Wrong mobile phone number %s"),
    WRONG_REQUEST_FORMAT("Wrong request format %s"),
    DATABASE_ERROR("Database error"),
    INVALID_JSON("Invalid request JSON"),
    EMPTY_VALUE("Empty value"),
    SAME_DAY_SAME_DOCTOR("Ticket to the same doctor same day exists"),
    PATIENT_IS_BUSY("Patient is busy %s"),
    SCHEDULE_NOT_EXISTS("Doctor has no appointments at the time"),
    UNKNOWN("Unknown server error");

    private String message;

    ServerErrorCode(String message) {
        this.message = message;
    }

    public String getErrorString() {
        return message;
    }
}
