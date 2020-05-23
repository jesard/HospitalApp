package net.thumbtack.school.hospital.error;

import java.util.ArrayList;
import java.util.List;

public class ServerException extends Exception {

    private List<MyError> errors = new ArrayList<>();

    public ServerException(List<MyError> errors) {
        this.errors = errors;
    }

    public ServerException(MyError myError) {
        errors.add(myError);
    }

    public List<MyError> getErrors() {
        return errors;
    }

    public void setErrors(List<MyError> errors) {
        this.errors = errors;
    }
}
