package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.request.RegAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegCommissionDtoRequest;
import net.thumbtack.school.hospital.dto.response.EmptyJsonResponse;
import net.thumbtack.school.hospital.dto.response.RegCommissionDtoResponse;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.service.DoctorService;
import net.thumbtack.school.hospital.service.PatientService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommissionsEndpoint {

    DoctorService doctorService = new DoctorService();
    PatientService patientService = new PatientService();

    @PostMapping(value = "/commissions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RegCommissionDtoResponse registerCommission(@RequestBody RegCommissionDtoRequest request, @CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) throws ServerException {
        return doctorService.registerCommission(request, token);
    }

    @DeleteMapping(value = "/commissions/{ticketNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyJsonResponse deleteCommission(@PathVariable("ticketNumber") String ticketNumber, @CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) throws ServerException {
        return patientService.deleteCommission(ticketNumber, token);
    }

}
