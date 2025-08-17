package com.example.ticket_service.application.service;


import com.example.ticket_service.domain.model.Ticket;

import java.util.List;


public interface TicketQueryService {
    Ticket getTicketById(Long id);

    List<Ticket> getTicketByStatus(String status);
    List<Ticket> getTicketByPriority(String priority);

}