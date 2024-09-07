package EmployeeSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import EmployeeSystem.enums.TicketStatus;
import EmployeeSystem.model.Ticket;
import EmployeeSystem.model.dto.TicketDto;
import EmployeeSystem.repository.TicketRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

class TicketServiceTest {

  @Mock private TicketRepository ticketRepository;

  @InjectMocks private TicketService ticketService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testGetTickets() {

    Ticket ticket1 = new Ticket(1, "subject-1", "desc 1", 1, 1, "some_status 1", "some_tag 1");
    Ticket ticket2 = new Ticket(1, "subject-2", "desc 2", 2, 2, "some_status 2", "some_tag 2");

    List<Ticket> tickets = Arrays.asList(ticket1, ticket2);

    // mock
    when(ticketRepository.findAll()).thenReturn(tickets);

    List<Ticket> result = ticketService.getTickets();

    assertEquals(tickets, result);
  }

//  @Test
//  void testGetTicketById() {
//
//    Ticket ticket1 = new Ticket(1, "subject-1", "desc 1", 1, 1, "some_status 1", "some_tag 1");
//
//    when(ticketRepository.findById(1)).thenReturn(Optional.of(ticket1));
//
//    Mono<Ticket> result = ticketService.getTicketById(1);
//
//    assertEquals(ticket1, result);
//  }

//  @Test
//  void testGetTicketById_NotFound() {
//
//    when(ticketRepository.findById(1)).thenReturn(Optional.empty());
//
//    Ticket result = ticketService.getTicketById(1);
//
//    assertEquals(null, result);
//  }

  @Test
  void testUpdateTicket() {

    // Mocked ticketDto
    TicketDto ticketDto = new TicketDto();
    ticketDto.setId(1);
    ticketDto.setStatus(TicketStatus.APPROVED.getName());

    // Mocked ticket
    Ticket ticket = new Ticket();
    ticket.setId(1);
    ticket.setStatus(TicketStatus.PENDING.getName());

    // Mocking behavior for deleteById and save methods
    // TODO : double check
    doNothing().when(ticketRepository).deleteById(ticketDto.getId());
    when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

    // Calling the method
    ticketService.updateTicket(ticketDto);

    // Verifying that deleteById and save were called with the correct arguments
    verify(ticketRepository, times(1)).deleteById(ticketDto.getId());
    verify(ticketRepository, times(1)).save(any(Ticket.class));
  }

  @Test
  void testAddTicket() {

    Ticket ticket = new Ticket();
    ticket.setStatus(TicketStatus.PENDING.getName());

    // mock
    when(ticketRepository.save(ticket)).thenReturn(ticket);

    ticketService.addTicket(ticket);

    verify(ticketRepository, times(1)).save(ticket);
  }
}
