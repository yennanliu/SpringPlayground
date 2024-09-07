package EmployeeSystem.controller;

import EmployeeSystem.common.ApiResponse;
import EmployeeSystem.model.Ticket;
import EmployeeSystem.model.dto.TicketDto;
import EmployeeSystem.service.TicketService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ticket")
public class TicketController {

  @Autowired TicketService ticketService;

  @GetMapping("/")
  public ResponseEntity<Flux<Ticket>> getTicket() {

    Flux<Ticket> tickets = ticketService.getTickets();
    return new ResponseEntity<>(tickets, HttpStatus.OK);
  }

  @GetMapping("/{ticketId}")
  public ResponseEntity<Ticket> getTicketById(@PathVariable("ticketId") Integer ticketId) {

    Ticket ticket = ticketService.getTicketById(ticketId);
    return new ResponseEntity<>(ticket, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<ApiResponse> updateTicket(@RequestBody TicketDto ticketDto) {

    ticketService.updateTicket(ticketDto);
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(true, "Ticket has been updated"), HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addDepartment(@RequestBody Ticket ticket) {

    ticketService.addTicket(ticket);
    return new ResponseEntity<>(new ApiResponse(true, "Ticket has been added"), HttpStatus.CREATED);
  }
}
