package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.Service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DoctorsEndpoint {

    DoctorService doctorService = new DoctorService();

    @PostMapping("/doctors")
    public ResponseEntity<String> insertDoctor(@RequestBody String requestJsonString) {
        String responseBody = doctorService.registerDoctor(requestJsonString);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

}
