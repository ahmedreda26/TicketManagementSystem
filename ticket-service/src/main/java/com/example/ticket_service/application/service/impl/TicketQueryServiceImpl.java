package com.example.ticket_service.application.service.impl;

import com.example.ticket_service.application.service.TicketQueryService;
import com.example.ticket_service.domain.enums.TicketPriority;
import com.example.ticket_service.domain.enums.TicketStatus;
import com.example.ticket_service.domain.model.Ticket;
import com.example.ticket_service.domain.repository.TicketQueryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TicketQueryServiceImpl implements TicketQueryService {


    private final TicketQueryRepository queryRepo;

    public TicketQueryServiceImpl( TicketQueryRepository queryRepo) {
        this.queryRepo = queryRepo;
    }

    @Override
    public Ticket getTicketById(Long id) {
        Ticket ticket = queryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with ID: " + id));
        return ticket;
    }

    @Override
    public List<Ticket> getTicketByStatus(String status) {
        List<Ticket> tickets = queryRepo.findByStatus(TicketStatus.valueOf(status))
                .orElseThrow(() -> new EntityNotFoundException("Ticket with Status: " + status + " not found"));
        return tickets;
    }

    @Override
    public List<Ticket> getTicketByPriority(String priority) {
        List<Ticket> tickets = queryRepo.findByPriority(TicketPriority.valueOf(priority))
                .orElseThrow(() -> new EntityNotFoundException("Ticket with Priority: " + priority + " not found"));
        return tickets;
    }

}
