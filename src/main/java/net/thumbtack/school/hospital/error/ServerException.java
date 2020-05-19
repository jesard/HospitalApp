package net.thumbtack.school.hospital.error;

import java.util.ArrayList;
import java.util.List;

public class ServerException extends Exception {

    private List<MyError> myErrors = new ArrayList<>();

    public ServerException(List<MyError> myErrors) {
        this.myErrors = myErrors;
    }

    public ServerException(MyError myError) {
        myErrors.add(myError);
    }

    public List<MyError> getMyErrors() {
        return myErrors;
    }

    public void setMyErrors(List<MyError> myErrors) {
        this.myErrors = myErrors;
    }
}
