package net.thumbtack.school.hospital.endpoint;


import net.thumbtack.school.hospital.dto.request.MakeAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.response.EmptyJsonResponse;
import net.thumbtack.school.hospital.dto.response.GetTicketsDtoResponse;
import net.thumbtack.school.hospital.dto.response.MakeAppointmentDtoResponse;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.service.PatientService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class TicketsEndpoint {

    PatientService patientService = new PatientService();

    @PostMapping(value = "/tickets", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public MakeAppointmentDtoResponse makeAppointment(@RequestBody @Valid MakeAppointmentDtoRequest request, @CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) throws ServerException {
        return patientService.makeAppointment(request, token);
    }

    @DeleteMapping(value = "/tickets/{ticketNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyJsonResponse deleteTicket(@PathVariable("ticketNumber") String ticketNumber, @CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) throws ServerException {
        return patientService.deleteTicket(ticketNumber, token);
    }

    @GetMapping(value = "/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetTicketsDtoResponse getTickets(@CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) throws ServerException {
        return patientService.getTickets(token);
    }


}
