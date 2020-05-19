package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.response.RegUserDtoResponse;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountEndpoint {

    UserService userService = new UserService();

    @GetMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
    public RegUserDtoResponse getUser(@CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) throws ServerException {
        return userService.getUserDtoResponseByToken(token);
    }


}
