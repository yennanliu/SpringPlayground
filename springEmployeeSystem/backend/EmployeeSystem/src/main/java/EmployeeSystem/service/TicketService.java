package EmployeeSystem.service;

import EmployeeSystem.enums.TicketStatus;
import EmployeeSystem.model.Ticket;
import EmployeeSystem.model.dto.TicketDto;
import EmployeeSystem.repository.TicketRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TicketService {

  @Autowired TicketRepository ticketRepository;

  public List<Ticket> getTickets() {

    return ticketRepository.findAll();
  }

  public Ticket getTicketById(Integer ticketId) {

    if (ticketRepository.findById(ticketId).isPresent()) {
      return ticketRepository.findById(ticketId).get();
    }
    log.warn("No ticket found with ticketId = " + ticketId);
    return null;
  }

  public void updateTicket(TicketDto ticketDto) {
    
    // Find existing ticket to preserve creation time and ID
    Ticket existingTicket = ticketRepository.findById(ticketDto.getId())
        .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketDto.getId()));
    
    // Update only the fields that should be modified
    existingTicket.setSubject(ticketDto.getSubject());
    existingTicket.setDescription(ticketDto.getDescription());
    existingTicket.setUserId(ticketDto.getUserId());
    existingTicket.setAssignedTo(ticketDto.getAssignedTo());
    existingTicket.setStatus(ticketDto.getStatus());
    existingTicket.setTag(ticketDto.getTag());
    
    // Save the updated ticket (updatedAt will be set automatically)
    ticketRepository.save(existingTicket);
  }

  public void addTicket(Ticket ticket) {

    // create ticket with "PENDING" as default status
    ticket.setStatus(TicketStatus.PENDING.getName());
    ticketRepository.save(ticket);
  }
}
