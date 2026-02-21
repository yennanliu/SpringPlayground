package EmployeeSystem.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DatabaseConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                // Ensure the tickets table exists with proper structure
                String createTicketsTable = "CREATE TABLE IF NOT EXISTS tickets (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "subject VARCHAR(255) NOT NULL," +
                    "description LONGTEXT," +
                    "user_id INT," +
                    "assigned_to INT," +
                    "status VARCHAR(50) DEFAULT 'PENDING'," +
                    "tag VARCHAR(100)," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                    "INDEX idx_user_id (user_id)," +
                    "INDEX idx_assigned_to (assigned_to)," +
                    "INDEX idx_status (status)," +
                    "INDEX idx_created_at (created_at)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;";

                try (Statement statement = connection.createStatement()) {
                    statement.execute(createTicketsTable);
                    log.info("✅ Tickets table created or verified successfully");
                    
                    // Verify table structure
                    java.sql.ResultSet resultSet = statement.executeQuery("DESCRIBE tickets");
                    log.info("📋 Tickets table structure:");
                    while (resultSet.next()) {
                        log.info("  {} - {} - {}", 
                            resultSet.getString("Field"),
                            resultSet.getString("Type"), 
                            resultSet.getString("Null"));
                    }
                }
                
            } catch (Exception e) {
                log.error("❌ Failed to initialize tickets table: {}", e.getMessage());
                throw new RuntimeException("Database initialization failed", e);
            }
        };
    }
} 