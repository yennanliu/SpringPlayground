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
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class TicketService {

  @Autowired TicketRepository ticketRepository;

  public Flux<Ticket> getTickets() {

    return ticketRepository.findAll();
  }

  public Ticket getTicketById(Integer ticketId) {

    Ticket ticket = ticketRepository.findById(ticketId).block();
    if (ticket != null) {
      return ticket;
    }
    log.warn("No ticket found with ticketId = " + ticketId);
    return null;
  }

  public void updateTicket(TicketDto ticketDto) {

    Ticket ticket = new Ticket();
    ticketRepository.deleteById(ticketDto.getId());
    BeanUtils.copyProperties(ticketDto, ticket);
    ticketRepository.save(ticket);
  }

  public void addTicket(Ticket ticket) {

    // create ticket with "PENDING" as default status
    ticket.setStatus(TicketStatus.PENDING.getName());
    ticketRepository.save(ticket);
  }
}
