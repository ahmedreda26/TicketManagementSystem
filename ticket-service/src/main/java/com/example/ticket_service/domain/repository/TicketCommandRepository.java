package com.example.ticket_service.domain.repository;

import com.example.ticket_service.domain.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketCommandRepository extends JpaRepository<Ticket,Long> {
}
