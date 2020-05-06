package net.thumbtack.school.hospital.debug;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class HttpGetTest {

    @GetMapping("/test")
    public ResponseEntity<String> testGet(HttpServletResponse response) {
        Cookie cookie = new Cookie("JAVASESSIONID", UUID.randomUUID().toString());
        response.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
