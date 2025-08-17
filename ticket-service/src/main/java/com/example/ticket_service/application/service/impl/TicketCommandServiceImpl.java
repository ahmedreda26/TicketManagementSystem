package com.example.ticket_service.application.service.impl;

import com.example.ticket_service.adapter.in.dto.command.TicketCreateRequestDTO;
import com.example.ticket_service.adapter.in.dto.command.TicketUpdateRequestDTO;
import com.example.ticket_service.application.mapper.TicketMapper;
import com.example.ticket_service.application.service.TicketCommandService;
import com.example.ticket_service.domain.enums.TicketPriority;
import com.example.ticket_service.domain.enums.TicketStatus;
import com.example.ticket_service.domain.model.Ticket;
import com.example.ticket_service.domain.repository.TicketCommandRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketCommandServiceImpl implements TicketCommandService {
    private final TicketCommandRepository commandRepo;

    private  final TicketMapper mapper;

    public TicketCommandServiceImpl(TicketCommandRepository commandRepo, TicketMapper mapper) {
        this.commandRepo = commandRepo;
        this.mapper = mapper;
    }

    @Override
    public Ticket createTicket(TicketCreateRequestDTO ticketDto) {
        Ticket ticket =new Ticket();
        ticket.setTitle(ticketDto.getTitle());
        ticket.setDescription(ticketDto.getDescription());
        ticket.setStatus(TicketStatus.valueOf(ticketDto.getStatus()));
        ticket.setPriority(TicketPriority.valueOf(ticketDto.getPriority()));
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setAssignedTo(ticketDto.getAssignedTo());
        return commandRepo.save(ticket);
    }

    @Override
    public Ticket updateTicket(Long id, TicketUpdateRequestDTO ticketDto) {
        Ticket existingTicket = commandRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));
        existingTicket.setTitle(ticketDto.getTitle());
        existingTicket.setDescription(ticketDto.getDescription());
        existingTicket.setStatus(TicketStatus.valueOf(ticketDto.getStatus()));
        existingTicket.setPriority(TicketPriority.valueOf(ticketDto.getPriority()));
        existingTicket.setAssignedTo(ticketDto.getAssignedTo());
        existingTicket.setUpdatedAt(LocalDateTime.now());
        return commandRepo.save(existingTicket);
    }

    @Override
    public void closeTicket(Long id) {
        Ticket existingTicket = commandRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));
        existingTicket.setStatus(TicketStatus.CLOSED);
        commandRepo.save(existingTicket);
    }
}
