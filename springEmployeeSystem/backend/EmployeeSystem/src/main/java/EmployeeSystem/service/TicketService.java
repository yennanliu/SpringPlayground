package EmployeeSystem.service;

import EmployeeSystem.enums.TicketStatus;
import EmployeeSystem.model.Ticket;
import EmployeeSystem.model.dto.AddTicketDto;
import EmployeeSystem.model.dto.TicketDto;
import EmployeeSystem.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class TicketService {

  @Autowired TicketRepository ticketRepository;

  public Flux<Ticket> getTickets() {

    return ticketRepository.findAll();
  }

  public Mono<Ticket> getTicketById(Integer ticketId) {

    Mono<Ticket> ticket = ticketRepository.findById(ticketId);
//    if (ticket != null) {
//      return ticket;
//    }
    //log.warn("No ticket found with ticketId = " + ticketId);
    return ticket;
  }

  public void updateTicket(TicketDto ticketDto) {

    Ticket ticket = new Ticket();
    ticketRepository.deleteById(ticketDto.getId());
    BeanUtils.copyProperties(ticketDto, ticket);
    ticketRepository.save(ticket);
  }

  public Mono<Ticket> addTicket(AddTicketDto addTicketDto) {
    Ticket newTicket = new Ticket();
    newTicket.setId(null);  // Ensure new entry
    newTicket.setStatus(TicketStatus.PENDING.getName());  // Setting default status
    newTicket.setDescription(addTicketDto.getDescription());
    newTicket.setUserId(addTicketDto.getUserId());
    newTicket.setSubject(addTicketDto.getSubject());  // Set subject
    newTicket.setTag(addTicketDto.getTag());  // Set tag

    return ticketRepository.save(newTicket)
            .doOnSuccess(savedTicket -> {
              System.out.println("Ticket saved: " + savedTicket);
            });
  }

}
