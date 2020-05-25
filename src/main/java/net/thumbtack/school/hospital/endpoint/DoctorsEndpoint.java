package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.request.DeleteDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.request.regdoctor.RegDocDtoRequest;
import net.thumbtack.school.hospital.dto.response.EmptyJsonResponse;
import net.thumbtack.school.hospital.dto.response.regdoctor.RegDoctorDtoResponse;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.service.DoctorService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DoctorsEndpoint {

    DoctorService doctorService = new DoctorService();

    @PostMapping(value = "/doctors", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RegDoctorDtoResponse insertDoctor(@RequestBody @Valid RegDocDtoRequest request, @CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) throws ServerException {
        return doctorService.registerDoctor(request, token);
    }

    @GetMapping(value = "/doctors/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RegDoctorDtoResponse getDoctorInfo(@CookieValue(value = "JAVASESSIONID", defaultValue = "") String token,
                                              @PathVariable("doctorId") int doctorId,
                                              @RequestParam(required = false, defaultValue = "no") String schedule,
                                              @RequestParam(required = false) String startDate,
                                              @RequestParam(required = false) String endDate) throws ServerException {
        return doctorService.getDoctor(doctorId, schedule, startDate, endDate, token);
    }

    @GetMapping(value = "/doctors", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RegDoctorDtoResponse> getDoctorsInfo(@CookieValue(value = "JAVASESSIONID", defaultValue = "") String token,
                                                     @RequestParam(required = false, defaultValue = "no") String schedule,
                                                     @RequestParam(required = false) String speciality,
                                                     @RequestParam(required = false) String startDate,
                                                     @RequestParam(required = false) String endDate) throws ServerException {
        return doctorService.getDoctors(schedule, speciality, startDate, endDate, token);
    }

    @PutMapping(value = "/doctors/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RegDoctorDtoResponse updateDoctorSchedule(@RequestBody RegDocDtoRequest request,
                                                     @PathVariable("doctorId") int doctorId,
                                                     @CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) throws ServerException {
        return doctorService.updateDoctorSchedule(request, doctorId, token);
    }


    @DeleteMapping(value = "/doctors/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyJsonResponse deleteDoctor(@RequestBody @Valid DeleteDoctorDtoRequest request,
                                          @PathVariable("doctorId") int doctorId,
                                          @CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) throws ServerException {
        return doctorService.deleteDoctor(request, doctorId, token);
    }
}
