package EmployeeSystem.controller;


import EmployeeSystem.model.Ticket;
import EmployeeSystem.model.dto.TicketDto;
import EmployeeSystem.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTicket() {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        when(ticketService.getTickets()).thenReturn(tickets);

        ResponseEntity<List<Ticket>> responseEntity = ticketController.getTicket();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(tickets, responseEntity.getBody());
    }

    @Test
    public void testGetTicketById() {
        Ticket ticket = new Ticket();
        when(ticketService.getTicketById(1)).thenReturn(ticket);

        ResponseEntity<Ticket> responseEntity = ticketController.getTicketById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ticket, responseEntity.getBody());
    }

    @Test
    public void testUpdateTicket() {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(1);
        ticketDto.setStatus("PENDING");

        ticketController.updateTicket(ticketDto);

        verify(ticketService, times(1)).updateTicket(ticketDto);
    }

    @Test
    public void testAddTicket() {
        Ticket ticket = new Ticket();

        ticketController.addDepartment(ticket);

        verify(ticketService, times(1)).addTicket(ticket);
    }
}
