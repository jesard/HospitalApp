package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class SessionsEndpoint {

	// REVU private
    UserService userService = new UserService();

    @PostMapping("/sessions")
    // REVU просто передайте методу LoginDtoRequest, а не String
    // контроллер сам его сделает
    // в других методах аналогично
    public ResponseEntity<String> login(@RequestBody String requestLoginJson, HttpServletResponse response) {
        String token = userService.login(requestLoginJson);
        Cookie cookie = new Cookie("JAVASESSIONID", token);
        response.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
