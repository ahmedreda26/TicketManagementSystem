package com.example.ticket_service.application.service;

import com.example.ticket_service.adapter.in.dto.command.TicketCreateRequestDTO;
import com.example.ticket_service.adapter.in.dto.command.TicketUpdateRequestDTO;
import com.example.ticket_service.domain.model.Ticket;

public interface TicketCommandService {
    Ticket createTicket(TicketCreateRequestDTO ticket);
    Ticket updateTicket(Long id, TicketUpdateRequestDTO ticket);
    void closeTicket(Long id);
}
