package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.HospitalServer;
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
public class SessionEndpoint {

    @PostMapping("/sessions")
    public ResponseEntity<String> login(@RequestBody String requestLoginJson, HttpServletResponse response) {
        Cookie cookie = new Cookie("JAVASESSIONID", UUID.randomUUID().toString());
        response.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
