package EmployeeSystem.service;

import EmployeeSystem.model.NotificationEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private MailService mailService;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSendVacationNotificationAsync() {
        // Arrange
        String userEmail = "user@example.com";
        Integer userId = 123;
        String vacationType = "Annual Leave";
        String startDate = "2024-01-15";
        String endDate = "2024-01-20";

        // Mock void method
        doNothing().when(mailService).sendMail(any(NotificationEmail.class));

        // Act
        CompletableFuture<Void> result = emailService.sendVacationNotificationAsync(
            userEmail, userId, vacationType, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertTrue(result.isDone());
        assertFalse(result.isCompletedExceptionally());
        
        // Verify that mailService.sendMail was called
        verify(mailService, times(1)).sendMail(any(NotificationEmail.class));
    }

    @Test
    void testSendAdminNotificationAsync() {
        // Arrange
        String adminEmail = "admin@example.com";
        Integer userId = 456;
        String vacationType = "Sick Leave";
        String startDate = "2024-02-10";
        String endDate = "2024-02-12";

        // Mock void method
        doNothing().when(mailService).sendMail(any(NotificationEmail.class));

        // Act
        CompletableFuture<Void> result = emailService.sendAdminNotificationAsync(
            adminEmail, userId, vacationType, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertTrue(result.isDone());
        assertFalse(result.isCompletedExceptionally());
        
        // Verify that mailService.sendMail was called
        verify(mailService, times(1)).sendMail(any(NotificationEmail.class));
    }

    @Test
    void testSendVacationStatusUpdateAsync() {
        // Arrange
        String userEmail = "employee@example.com";
        Integer userId = 789;
        String vacationType = "Personal Leave";
        String status = "APPROVED";
        String startDate = "2024-03-05";
        String endDate = "2024-03-07";

        // Mock void method
        doNothing().when(mailService).sendMail(any(NotificationEmail.class));

        // Act
        CompletableFuture<Void> result = emailService.sendVacationStatusUpdateAsync(
            userEmail, userId, vacationType, status, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertTrue(result.isDone());
        assertFalse(result.isCompletedExceptionally());
        
        // Verify that mailService.sendMail was called
        verify(mailService, times(1)).sendMail(any(NotificationEmail.class));
    }

    @Test
    void testSendWelcomeEmailAsync() {
        // Arrange
        String userEmail = "newuser@example.com";
        String firstName = "John";
        String lastName = "Doe";

        // Mock void method
        doNothing().when(mailService).sendMail(any(NotificationEmail.class));

        // Act
        CompletableFuture<Void> result = emailService.sendWelcomeEmailAsync(
            userEmail, firstName, lastName);

        // Assert
        assertNotNull(result);
        assertTrue(result.isDone());
        assertFalse(result.isCompletedExceptionally());
        
        // Verify that mailService.sendMail was called
        verify(mailService, times(1)).sendMail(any(NotificationEmail.class));
    }

    @Test
    void testSendVacationNotificationAsync_WithException() {
        // Arrange
        String userEmail = "user@example.com";
        Integer userId = 123;
        String vacationType = "Annual Leave";
        String startDate = "2024-01-15";
        String endDate = "2024-01-20";

        // Mock to throw exception
        doThrow(new RuntimeException("Email sending failed"))
            .when(mailService).sendMail(any(NotificationEmail.class));

        // Act
        CompletableFuture<Void> result = emailService.sendVacationNotificationAsync(
            userEmail, userId, vacationType, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertTrue(result.isDone());
        assertTrue(result.isCompletedExceptionally());
        
        // Verify exception
        assertThrows(Exception.class, result::get);
        
        // Verify that mailService.sendMail was called
        verify(mailService, times(1)).sendMail(any(NotificationEmail.class));
    }

    @Test
    void testEmailBodyBuilding() {
        // This test verifies that email bodies are constructed properly
        // by checking that the service methods complete without throwing exceptions
        
        String userEmail = "test@example.com";
        Integer userId = 100;
        String vacationType = "Test Leave";
        String startDate = "2024-01-01";
        String endDate = "2024-01-05";

        doNothing().when(mailService).sendMail(any(NotificationEmail.class));

        // Test all email types
        assertDoesNotThrow(() -> {
            emailService.sendVacationNotificationAsync(userEmail, userId, vacationType, startDate, endDate);
            emailService.sendAdminNotificationAsync("admin@test.com", userId, vacationType, startDate, endDate);
            emailService.sendVacationStatusUpdateAsync(userEmail, userId, vacationType, "APPROVED", startDate, endDate);
            emailService.sendWelcomeEmailAsync(userEmail, "Test", "User");
        });

        // Verify all methods were called
        verify(mailService, times(4)).sendMail(any(NotificationEmail.class));
    }
} 