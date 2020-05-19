package net.thumbtack.school.hospital.error;

public enum ServerErrorCode {
    USER_DUPLICATE("User duplicate"),
    USER_NOT_FOUND("User not found"),
    WRONG_LOGIN_OR_PASSWORD("Wrong login or password"),
    WRONG_PASSWORD("Wrong password"),
    INVALID_LOGIN("Invalid login"),
    WRONG_RUSSIAN_NAME("Wrong russian name"),
    MAX_NAME_LENGTH("Max name length exceeded"),
    PASSWORD_MIN_LENGTH("Too short password"),
    ROOM_IS_BUSY("Room is busy"),
    ROOM_NOT_FOUND("Room not found"),
    WRONG_SCHEDULE("Wrong schedule"),
    WRONG_USER("User have no required permissions"),
    DOCTOR_IS_BUSY("Doctor is busy"),
    SLOT_IS_BUSY("The time is not free"),
    WRONG_ROOM("Wrong room"),
    WRONG_DATE("Wrong date"),
    WRONG_TIME("Wrong time"),
    WRONG_DAY_OF_WEEK("Wrong day of week"),
    SPECIALITY_NOT_FOUND("Speciality not found"),
    ONE_NOT_NULL("One value should be empty"),
    WRONG_MOBILE_PHONE("Wrong mobile phone number"),
    WRONG_REQUEST_FORMAT("Wrong request format"),
    DATABASE_ERROR("Database error"),
    INVALID_JSON("Invalid request JSON"),
    EMPTY_VALUE("Empty value"),
    UNKNOWN("Unknown server error");

    private String message;

    ServerErrorCode(String message) {
        this.message = message;
    }

    public String getErrorString() {
        return message;
    }
}
