package EmployeeSystem.repository;

import EmployeeSystem.model.Ticket;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends R2dbcRepository<Ticket, Integer> {}
