package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.dto.response.regdoctor.TicketDtoResponse;

import java.util.List;

public class GetTicketsDtoResponse {

    private List<TicketDtoResponse> tickets;

    public GetTicketsDtoResponse(List<TicketDtoResponse> tickets) {
        this.tickets = tickets;
    }

    public GetTicketsDtoResponse() {
    }

    public List<TicketDtoResponse> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDtoResponse> tickets) {
        this.tickets = tickets;
    }
}
