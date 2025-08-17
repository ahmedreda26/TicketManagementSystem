package com.example.ticket_service.adapter.in.controller;

import com.example.ticket_service.adapter.in.dto.command.TicketCreateRequestDTO;
import com.example.ticket_service.adapter.in.dto.command.TicketUpdateRequestDTO;
import com.example.ticket_service.application.service.TicketCommandService;
import com.example.ticket_service.application.service.TicketQueryService;
import com.example.ticket_service.domain.model.Ticket;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/api/tickets")
@RestController
public class TicketCommandController {

    private final TicketCommandService commandService;

    private final TicketQueryService queryService;

    public TicketCommandController(TicketCommandService commandService, TicketQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getById(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.getTicketById(id));
    }

    @GetMapping("/status")
    public ResponseEntity<List<Ticket>> getByStatus(@RequestParam String status) {
        return ResponseEntity.ok(queryService.getTicketByStatus(status));
    }

    @GetMapping("/priority")
    public ResponseEntity<List<Ticket>> getByPriority(@RequestParam String priority) {
        return ResponseEntity.ok(queryService.getTicketByPriority(priority));
    }

    @PostMapping
    public ResponseEntity<Ticket> create(@RequestBody TicketCreateRequestDTO ticket) {
        Ticket savedTicket = commandService.createTicket(ticket);
        return ResponseEntity.ok(savedTicket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> update(@PathVariable Long id, @RequestBody TicketUpdateRequestDTO ticket) {
        Ticket updated = commandService.updateTicket(id, ticket);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commandService.closeTicket(id);
        return ResponseEntity.noContent().build();
    }




}
