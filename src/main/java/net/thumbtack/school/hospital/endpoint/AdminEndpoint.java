package net.thumbtack.school.hospital.endpoint;


import net.thumbtack.school.hospital.HospitalServer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminEndpoint {

    @PostMapping("/admin")
    public String insertAdmin(@RequestBody String requestJsonString) {
        return HospitalServer.insertAdmin(requestJsonString);
    }

}
