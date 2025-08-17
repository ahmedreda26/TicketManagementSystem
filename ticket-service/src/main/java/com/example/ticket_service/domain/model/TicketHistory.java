package com.example.ticket_service.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistory {

    @Id
    private Long id;

    @Column(name = "ticket_id")
    private Long ticketId;

    private String action;

    @Column(name = "changed_by")
    private Long changedBy;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;
}