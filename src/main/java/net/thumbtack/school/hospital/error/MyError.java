package net.thumbtack.school.hospital.error;

public class MyError {
    private ServerErrorCode errorCode;
    private String field;
    private String message;

    public MyError(ServerErrorCode errorCode, Field field) {
        this.errorCode = errorCode;
        this.field = field.getFieldString();
        this.message = errorCode.getErrorString();
    }

    public MyError(ServerErrorCode errorCode, Field field, String param) {
        this.errorCode = errorCode;
        this.field = field.getFieldString();
        this.message = String.format(errorCode.getErrorString(), param);
    }


    public MyError(String code, String field, String message, String param) {
        this.errorCode = convert(code);
        this.field = field;
        this.message = String.format(message, param);
    }

    public MyError(ServerErrorCode errorCode, Field field, String message, String param) {
        this.errorCode = errorCode;
        this.field = field.getFieldString();
        this.message = message;
    }


    public MyError() {
    }

    private ServerErrorCode convert(String code) {
        switch (code) {
            case "RussianName": return ServerErrorCode.INVALID_RUSSIAN_NAME;
            case "Login": return ServerErrorCode.INVALID_LOGIN;
            case "MaxNameLength": return ServerErrorCode.MAX_NAME_LENGTH;
            case "PasswordMinLength": return ServerErrorCode.PASSWORD_MIN_LENGTH;
            case "Date": return ServerErrorCode.INVALID_DATE;
            case "Time": return ServerErrorCode.INVALID_TIME;
            case "WeekDay":
            case "WeekDays":
                return ServerErrorCode.INVALID_DAY_OF_WEEK;
            case "OneNotNull": return ServerErrorCode.ONE_NOT_NULL;
            case "MobilePhone": return ServerErrorCode.INVALID_MOBILE_PHONE;
            case "NotEmpty": return ServerErrorCode.EMPTY_VALUE;
            case "Email": return ServerErrorCode.INVALID_EMAIL;
            default: return ServerErrorCode.UNKNOWN;
        }
    }

    public ServerErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ServerErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
