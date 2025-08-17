package com.example.ticket_service.domain.repository;

import com.example.ticket_service.domain.enums.TicketPriority;
import com.example.ticket_service.domain.enums.TicketStatus;
import com.example.ticket_service.domain.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TicketQueryRepository extends JpaRepository<Ticket, Long> {
    Optional<List<Ticket>> findByStatus(TicketStatus status);

    Optional<List<Ticket>> findByPriority(TicketPriority priority);

}
