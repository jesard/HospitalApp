package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.error.MyError;

import java.util.List;

public class ErrorDtoResponse {

    private List<MyError> myErrors;

    public ErrorDtoResponse(List<MyError> myErrors) {
        this.myErrors = myErrors;
    }

    public List<MyError> getMyErrors() {
        return myErrors;
    }

    public void setMyErrors(List<MyError> myErrors) {
        this.myErrors = myErrors;
    }
}
