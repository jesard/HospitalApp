package net.thumbtack.school.hospital.Service;

import net.thumbtack.school.hospital.dto.request.RegUserDtoRequest;
import net.thumbtack.school.hospital.dto.response.RegUserDtoResponse;
import net.thumbtack.school.hospital.model.User;

public class UserService {

    public static void makeUserFromDtoRequest(User user, RegUserDtoRequest request) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPatronymic(request.getPatronymic());
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
    }

    public static void makeDtoResponseFromUser(RegUserDtoResponse response, User user) {
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPatronymic(user.getPatronymic());
    }
}
