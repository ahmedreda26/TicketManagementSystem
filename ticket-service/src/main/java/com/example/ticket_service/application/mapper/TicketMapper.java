package com.example.ticket_service.application.mapper;

import com.example.ticket_service.adapter.in.dto.command.TicketCreateRequestDTO;
import com.example.ticket_service.adapter.in.dto.command.TicketUpdateRequestDTO;
import com.example.ticket_service.adapter.in.dto.query.TicketResponseDTO;
import com.example.ticket_service.domain.model.Ticket;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    Ticket toEntity(TicketCreateRequestDTO dto);
    TicketResponseDTO toResponseDTO(Ticket ticket);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTicketFromDto(TicketUpdateRequestDTO dto, @MappingTarget Ticket ticket);
}
