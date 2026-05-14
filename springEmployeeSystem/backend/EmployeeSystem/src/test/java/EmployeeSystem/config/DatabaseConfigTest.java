package EmployeeSystem.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import EmployeeSystem.model.Ticket;
import EmployeeSystem.repository.TicketRepository;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class DatabaseConfigTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    public void testTicketTableCreation() {
        // Test that we can create and save a ticket
        Ticket ticket = new Ticket();
        ticket.setSubject("Test Ticket");
        ticket.setDescription("This is a test ticket to verify table creation");
        ticket.setUserId(1);
        ticket.setAssignedTo(2);
        ticket.setStatus("PENDING");
        ticket.setTag("Test");

        // Save the ticket
        Ticket savedTicket = ticketRepository.save(ticket);

        // Verify it was saved correctly
        assertThat(savedTicket.getId()).isNotNull();
        assertThat(savedTicket.getSubject()).isEqualTo("Test Ticket");
        assertThat(savedTicket.getCreatedAt()).isNotNull();
        assertThat(savedTicket.getUpdatedAt()).isNotNull();
    }

    @Test
    public void testTicketRepository() {
        // Test that the repository works correctly
        long initialCount = ticketRepository.count();
        
        Ticket ticket = new Ticket();
        ticket.setSubject("Repository Test");
        ticket.setDescription("Testing repository functionality");
        ticket.setUserId(1);
        ticket.setStatus("PENDING");
        
        ticketRepository.save(ticket);
        
        long finalCount = ticketRepository.count();
        assertThat(finalCount).isEqualTo(initialCount + 1);
    }
} 