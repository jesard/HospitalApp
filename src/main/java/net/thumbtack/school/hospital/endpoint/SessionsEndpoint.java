package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.response.EmptyJsonResponse;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class SessionsEndpoint {

    private UserService userService = new UserService();

    @PostMapping(value = "/sessions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody LoginDtoRequest request, HttpServletResponse response) throws ServerException {
        String token = userService.login(request);
        Cookie cookie = new Cookie("JAVASESSIONID", token);
        response.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyJsonResponse logout(@CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) {
        return userService.logout(token);
    }


}
