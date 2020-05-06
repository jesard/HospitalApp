package net.thumbtack.school.hospital.endpoint;


import net.thumbtack.school.hospital.Service.AdminService;
import net.thumbtack.school.hospital.Service.PatientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TicketsEndpoint {

    PatientService patientService = new PatientService();

    @PostMapping("/tickets")
    public String insertAdmin(@RequestBody String requestAppointmentJson, @CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) {
        return patientService.makeAppointment(requestAppointmentJson, token);
    }


}
