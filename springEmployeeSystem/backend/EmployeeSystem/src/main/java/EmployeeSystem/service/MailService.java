package EmployeeSystem.service;

import EmployeeSystem.exception.CustomException;
import EmployeeSystem.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

  private final JavaMailSender mailSender;
  private final MailContentBuilder mailContentBuilder;
  private final String adminEmail = "employee_admin@dev.com";

  /**
   * Sends email asynchronously using dedicated email thread pool
   * This method runs in a separate thread to avoid blocking the main application thread
   * 
   * @param notificationEmail Email details including recipient, subject, and body
   */
  @Async("emailTaskExecutor")
  public void sendMail(NotificationEmail notificationEmail) {
    
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    String threadName = Thread.currentThread().getName();
    
    log.info("[{}] Starting email sending process in thread: {} for recipient: {}", 
             timestamp, threadName, notificationEmail.getRecipient());

    MimeMessagePreparator messagePreparator = mimeMessage -> {
      try {
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(adminEmail);
        messageHelper.setTo(notificationEmail.getRecipient());
        messageHelper.setSubject(notificationEmail.getSubject());
        
        // Use HTML content if available, otherwise use plain text
        String emailBody = mailContentBuilder.build(notificationEmail.getBody());
        messageHelper.setText(emailBody, true); // true indicates HTML content
        
        log.debug("[{}] Email message prepared for: {}", timestamp, notificationEmail.getRecipient());
        
      } catch (Exception e) {
        log.error("[{}] Error preparing email message: {}", timestamp, e.getMessage());
        throw new RuntimeException("Failed to prepare email message", e);
      }
    };

    try {
      // Send the email
      mailSender.send(messagePreparator);
      
      log.info("[{}] ✅ Email sent successfully in thread: {} to: {} with subject: '{}'", 
               timestamp, threadName, notificationEmail.getRecipient(), notificationEmail.getSubject());
      
    } catch (MailException e) {
      log.error("[{}] ❌ Mail sending failed in thread: {} for recipient: {} - Error: {}", 
                timestamp, threadName, notificationEmail.getRecipient(), e.getMessage());
      
      // Don't throw exception here to prevent breaking the async flow
      // Instead, log the error and potentially implement retry logic or dead letter queue
      handleEmailFailure(notificationEmail, e, timestamp);
    }
  }

  /**
   * Handles email sending failures
   * This can be extended to implement retry logic, dead letter queues, etc.
   */
  private void handleEmailFailure(NotificationEmail notificationEmail, Exception e, String timestamp) {
    log.warn("[{}] Email failure handled for: {} - Consider implementing retry mechanism", 
             timestamp, notificationEmail.getRecipient());
    
    // TODO: Implement retry logic or store failed emails for later processing
    // For now, we just log the failure
  }

  /**
   * Sends email with retry mechanism (future enhancement)
   */
  @Async("emailTaskExecutor")
  public void sendMailWithRetry(NotificationEmail notificationEmail, int maxRetries) {
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    String threadName = Thread.currentThread().getName();
    
    log.info("[{}] Starting email sending with retry in thread: {} for recipient: {} (max retries: {})", 
             timestamp, threadName, notificationEmail.getRecipient(), maxRetries);

    for (int attempt = 1; attempt <= maxRetries; attempt++) {
      try {
        sendMail(notificationEmail);
        log.info("[{}] Email sent successfully on attempt {} for: {}", 
                 timestamp, attempt, notificationEmail.getRecipient());
        return; // Success, exit retry loop
        
      } catch (Exception e) {
        log.warn("[{}] Email sending attempt {} failed for: {} - Error: {}", 
                 timestamp, attempt, notificationEmail.getRecipient(), e.getMessage());
        
        if (attempt == maxRetries) {
          log.error("[{}] All {} email sending attempts failed for: {}", 
                    timestamp, maxRetries, notificationEmail.getRecipient());
          handleEmailFailure(notificationEmail, e, timestamp);
        } else {
          // Wait before retry (exponential backoff)
          try {
            Thread.sleep(1000 * attempt); // Wait 1s, 2s, 3s, etc.
          } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            log.error("Email retry interrupted for: {}", notificationEmail.getRecipient());
            return;
          }
        }
      }
    }
  }
}
