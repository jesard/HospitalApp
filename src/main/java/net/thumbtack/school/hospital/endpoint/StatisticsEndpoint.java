package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.response.StatsDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.response.StatsDoctorsDtoResponse;
import net.thumbtack.school.hospital.dto.response.StatsPatientDtoResponse;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.service.StatisticsService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsEndpoint {

    private StatisticsService statisticsService = new StatisticsService();

    @GetMapping(value = "/doctors/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatsDoctorDtoResponse getDoctorStatistics(@CookieValue(value = "JAVASESSIONID", defaultValue = "") String token,
                                                      @PathVariable("doctorId") int doctorId,
                                                      @RequestParam(required = false) String startDate,
                                                      @RequestParam(required = false) String endDate) throws ServerException {
        return statisticsService.getDoctorStatistics(doctorId, startDate, endDate, token);
    }

    @GetMapping(value = "/doctors", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatsDoctorsDtoResponse getDoctorsStatistics(@CookieValue(value = "JAVASESSIONID", defaultValue = "") String token,
                                                        @RequestParam(required = false) String speciality,
                                                        @RequestParam(required = false) String startDate,
                                                        @RequestParam(required = false) String endDate,
                                                        @RequestParam(required = false, defaultValue = "no") String detailed) throws ServerException {
        return statisticsService.getDoctorsStatistics(speciality, startDate, endDate, detailed, token);
    }

    @GetMapping(value = "/patients/{patientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatsPatientDtoResponse getPatientStatistics(@CookieValue(value = "JAVASESSIONID", defaultValue = "") String token,
                                                        @PathVariable("patientId") int patientId,
                                                        @RequestParam(required = false) String startDate,
                                                        @RequestParam(required = false) String endDate) throws ServerException {
        return statisticsService.getPatientStatistics(patientId, startDate, endDate, token);
    }




}
