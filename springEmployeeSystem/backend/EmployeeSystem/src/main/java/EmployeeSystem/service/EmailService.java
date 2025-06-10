package EmployeeSystem.service;

import EmployeeSystem.model.NotificationEmail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

/**
 * Email Service - Facade for email operations
 * This service provides a high-level interface for email functionality
 * and uses dedicated thread pools for async processing
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final MailService mailService;
    
    /**
     * Send a vacation notification email asynchronously
     */
    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendVacationNotificationAsync(
            String userEmail, 
            Integer userId, 
            String vacationType, 
            String startDate, 
            String endDate) {
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("[{}] Processing vacation notification email for user: {} ({})", timestamp, userId, userEmail);
        
        try {
            NotificationEmail notificationEmail = new NotificationEmail(
                "Vacation Request Submitted - " + userId + " - " + vacationType,
                userEmail,
                buildVacationEmailBody(userId, vacationType, startDate, endDate)
            );
            
            mailService.sendMail(notificationEmail);
            
            log.info("[{}] Vacation notification email queued successfully for user: {}", timestamp, userId);
            return CompletableFuture.completedFuture(null);
            
        } catch (Exception e) {
            log.error("[{}] Failed to process vacation notification email for user: {} - Error: {}", 
                     timestamp, userId, e.getMessage());
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
    
    /**
     * Send admin notification email asynchronously  
     */
    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendAdminNotificationAsync(
            String adminEmail,
            Integer userId, 
            String vacationType, 
            String startDate, 
            String endDate) {
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("[{}] Processing admin notification email for vacation request from user: {}", timestamp, userId);
        
        try {
            NotificationEmail notificationEmail = new NotificationEmail(
                "New Vacation Request - User " + userId + " - " + vacationType,
                adminEmail,
                buildAdminNotificationBody(userId, vacationType, startDate, endDate)
            );
            
            mailService.sendMail(notificationEmail);
            
            log.info("[{}] Admin notification email queued successfully for vacation request from user: {}", 
                     timestamp, userId);
            return CompletableFuture.completedFuture(null);
            
        } catch (Exception e) {
            log.error("[{}] Failed to process admin notification email for user: {} - Error: {}", 
                     timestamp, userId, e.getMessage());
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
    
    /**
     * Send vacation status update notification
     */
    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendVacationStatusUpdateAsync(
            String userEmail,
            Integer userId,
            String vacationType,
            String status,
            String startDate,
            String endDate) {
            
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("[{}] Processing vacation status update email for user: {} - Status: {}", 
                 timestamp, userId, status);
        
        try {
            NotificationEmail notificationEmail = new NotificationEmail(
                "Vacation Request " + status + " - " + vacationType,
                userEmail,
                buildVacationStatusUpdateBody(userId, vacationType, status, startDate, endDate)
            );
            
            mailService.sendMail(notificationEmail);
            
            log.info("[{}] Vacation status update email queued successfully for user: {}", timestamp, userId);
            return CompletableFuture.completedFuture(null);
            
        } catch (Exception e) {
            log.error("[{}] Failed to process vacation status update email for user: {} - Error: {}", 
                     timestamp, userId, e.getMessage());
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
    
    /**
     * Send welcome email to new users
     */
    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendWelcomeEmailAsync(
            String userEmail, 
            String firstName, 
            String lastName) {
            
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("[{}] Processing welcome email for new user: {} {}", timestamp, firstName, lastName);
        
        try {
            NotificationEmail notificationEmail = new NotificationEmail(
                "Welcome to Employee System - " + firstName + " " + lastName,
                userEmail,
                buildWelcomeEmailBody(firstName, lastName)
            );
            
            mailService.sendMail(notificationEmail);
            
            log.info("[{}] Welcome email queued successfully for user: {} {}", timestamp, firstName, lastName);
            return CompletableFuture.completedFuture(null);
            
        } catch (Exception e) {
            log.error("[{}] Failed to process welcome email for user: {} {} - Error: {}", 
                     timestamp, firstName, lastName, e.getMessage());
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
    
    // Email body building methods
    
    private String buildVacationEmailBody(Integer userId, String vacationType, String startDate, String endDate) {
        return String.format(
            "Dear Employee (ID: %d),\n\n" +
            "Your vacation request has been submitted successfully and is now under review.\n\n" +
            "Vacation Details:\n" +
            "• Type: %s\n" +
            "• Start Date: %s\n" +
            "• End Date: %s\n" +
            "• Status: Pending Review\n\n" +
            "You will receive a notification once your request has been reviewed.\n\n" +
            "Thank you for using the Employee System.\n\n" +
            "Best regards,\n" +
            "HR Team", 
            userId, vacationType, startDate, endDate);
    }
    
    private String buildAdminNotificationBody(Integer userId, String vacationType, String startDate, String endDate) {
        return String.format(
            "Dear Admin,\n\n" +
            "A new vacation request has been submitted and requires your review.\n\n" +
            "Request Details:\n" +
            "• Employee ID: %d\n" +
            "• Vacation Type: %s\n" +
            "• Start Date: %s\n" +
            "• End Date: %s\n" +
            "• Status: Pending Review\n\n" +
            "Please log in to the Employee System to review and approve/reject this request.\n\n" +
            "Best regards,\n" +
            "Employee System", 
            userId, vacationType, startDate, endDate);
    }
    
    private String buildVacationStatusUpdateBody(Integer userId, String vacationType, String status, 
                                               String startDate, String endDate) {
        return String.format(
            "Dear Employee (ID: %d),\n\n" +
            "Your vacation request has been %s.\n\n" +
            "Vacation Details:\n" +
            "• Type: %s\n" +
            "• Start Date: %s\n" +
            "• End Date: %s\n" +
            "• Status: %s\n\n" +
            "%s\n\n" +
            "Best regards,\n" +
            "HR Team", 
            userId, status.toLowerCase(), vacationType, startDate, endDate, status,
            status.equalsIgnoreCase("APPROVED") ? 
                "Enjoy your time off!" : 
                "Please contact HR if you have any questions about this decision.");
    }
    
    private String buildWelcomeEmailBody(String firstName, String lastName) {
        return String.format(
            "Dear %s %s,\n\n" +
            "Welcome to the Employee System!\n\n" +
            "Your account has been successfully created. You can now:\n" +
            "• Submit vacation requests\n" +
            "• View your vacation history\n" +
            "• Update your profile information\n" +
            "• Submit support tickets\n\n" +
            "If you need any assistance, please don't hesitate to contact the HR team.\n\n" +
            "Welcome aboard!\n\n" +
            "Best regards,\n" +
            "HR Team", 
            firstName, lastName);
    }
} 