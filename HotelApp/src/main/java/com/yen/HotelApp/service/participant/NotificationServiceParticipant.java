package com.yen.HotelApp.service.participant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yen.HotelApp.dto.BookingRequest;
import com.yen.HotelApp.entity.PreparedTransaction;
import com.yen.HotelApp.repository.PreparedTransactionRepository;
import com.yen.HotelApp.transaction.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class NotificationServiceParticipant implements TransactionParticipant {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceParticipant.class);
    private static final String PARTICIPANT_ID = "NOTIFICATION_SERVICE";
    private static final Random random = new Random();
    
    @Autowired
    private PreparedTransactionRepository preparedTransactionRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    @Transactional
    public ParticipantState prepare(String transactionId, TransactionContext context) {
        logger.info("Preparing notifications for transaction: {}", transactionId);
        
        try {
            BookingRequest request = (BookingRequest) context.getTransactionData();
            
            if (request.getGuestEmail() == null || request.getGuestEmail().trim().isEmpty()) {
                logger.warn("Missing guest email for transaction: {}", transactionId);
                return ParticipantState.ABORTED;
            }
            
            if (!isValidEmail(request.getGuestEmail())) {
                logger.warn("Invalid email format for transaction: {}", transactionId);
                return ParticipantState.ABORTED;
            }
            
            Thread.sleep(random.nextInt(300) + 100);
            
            NotificationData notificationData = new NotificationData();
            notificationData.transactionId = transactionId;
            notificationData.guestEmail = request.getGuestEmail();
            notificationData.guestName = request.getGuestName();
            notificationData.checkInDate = request.getCheckInDate();
            notificationData.checkOutDate = request.getCheckOutDate();
            notificationData.totalPrice = request.getTotalPrice();
            
            NotificationMessage confirmationEmail = new NotificationMessage();
            confirmationEmail.messageId = "EMAIL_" + UUID.randomUUID().toString();
            confirmationEmail.type = "EMAIL";
            confirmationEmail.recipient = request.getGuestEmail();
            confirmationEmail.subject = "Hotel Booking Confirmation";
            confirmationEmail.content = buildConfirmationEmailContent(request);
            confirmationEmail.status = "QUEUED";
            
            NotificationMessage smsReminder = new NotificationMessage();
            smsReminder.messageId = "SMS_" + UUID.randomUUID().toString();
            smsReminder.type = "SMS";
            smsReminder.recipient = request.getGuestEmail(); // In real scenario, this would be phone number
            smsReminder.content = buildSmsReminderContent(request);
            smsReminder.status = "QUEUED";
            
            notificationData.messages = new ArrayList<>();
            notificationData.messages.add(confirmationEmail);
            notificationData.messages.add(smsReminder);
            
            PreparedTransaction preparedTx = new PreparedTransaction(
                transactionId,
                PARTICIPANT_ID,
                serializeNotificationData(notificationData),
                LocalDateTime.now().plusSeconds(context.getTimeoutSeconds())
            );
            preparedTransactionRepository.save(preparedTx);
            
            logger.info("Notifications prepared for transaction: {} with {} messages", 
                       transactionId, notificationData.messages.size());
            return ParticipantState.PREPARED;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Notification preparation interrupted for transaction: {}", transactionId);
            return ParticipantState.ABORTED;
        } catch (Exception e) {
            logger.error("Error preparing notifications for transaction: {}", transactionId, e);
            return ParticipantState.ABORTED;
        }
    }
    
    @Override
    @Transactional
    public void commit(String transactionId) {
        logger.info("Committing notifications for transaction: {}", transactionId);
        
        try {
            Optional<PreparedTransaction> preparedTxOpt = 
                preparedTransactionRepository.findByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
            if (preparedTxOpt.isEmpty()) {
                logger.warn("No prepared notifications found for commit: {}", transactionId);
                return;
            }
            
            PreparedTransaction preparedTx = preparedTxOpt.get();
            NotificationData notificationData = deserializeNotificationData(preparedTx.getPreparedData());
            
            for (NotificationMessage message : notificationData.messages) {
                Thread.sleep(random.nextInt(200) + 100);
                
                // Simulate sending notification with 95% success rate
                if (random.nextDouble() < 0.95) {
                    message.status = "SENT";
                    message.sentAt = LocalDateTime.now();
                    logger.info("Notification sent: {} to {}", message.messageId, message.recipient);
                } else {
                    message.status = "FAILED";
                    message.failureReason = "Delivery timeout";
                    logger.warn("Notification failed: {} to {}", message.messageId, message.recipient);
                }
            }
            
            logger.info("Notifications committed for transaction: {}", transactionId);
            preparedTransactionRepository.deleteByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Notification commit interrupted for transaction: {}", transactionId);
        } catch (Exception e) {
            logger.error("Error committing notifications for transaction: {}", transactionId, e);
        }
    }
    
    @Override
    @Transactional
    public void rollback(String transactionId) {
        logger.info("Rolling back notifications for transaction: {}", transactionId);
        
        try {
            Optional<PreparedTransaction> preparedTxOpt = 
                preparedTransactionRepository.findByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
            if (preparedTxOpt.isEmpty()) {
                logger.warn("No prepared notifications found for rollback: {}", transactionId);
                return;
            }
            
            PreparedTransaction preparedTx = preparedTxOpt.get();
            NotificationData notificationData = deserializeNotificationData(preparedTx.getPreparedData());
            
            Thread.sleep(random.nextInt(100) + 50);
            
            for (NotificationMessage message : notificationData.messages) {
                message.status = "CANCELLED";
                message.cancelledAt = LocalDateTime.now();
            }
            
            logger.info("Notifications cancelled for transaction: {}", transactionId);
            preparedTransactionRepository.deleteByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Notification rollback interrupted for transaction: {}", transactionId);
        } catch (Exception e) {
            logger.error("Error rolling back notifications for transaction: {}", transactionId, e);
        }
    }
    
    @Override
    public String getParticipantId() {
        return PARTICIPANT_ID;
    }
    
    @Override
    public boolean canTimeout() {
        return false; // Notifications can be sent later if needed
    }
    
    @Override
    public int getTimeoutSeconds() {
        return 600; // 10 minutes (longer timeout for notifications)
    }
    
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }
    
    private String buildConfirmationEmailContent(BookingRequest request) {
        return String.format(
            "Dear %s,\n\nYour hotel booking has been confirmed!\n\n" +
            "Check-in: %s\nCheck-out: %s\nTotal Price: $%.2f\n\n" +
            "Thank you for choosing our hotel!\n\nBest regards,\nHotel Management",
            request.getGuestName(),
            request.getCheckInDate(),
            request.getCheckOutDate(),
            request.getTotalPrice()
        );
    }
    
    private String buildSmsReminderContent(BookingRequest request) {
        return String.format(
            "Hotel booking confirmed for %s. Check-in: %s. Total: $%.2f. See you soon!",
            request.getGuestName(),
            request.getCheckInDate(),
            request.getTotalPrice()
        );
    }
    
    private String serializeNotificationData(NotificationData notificationData) {
        try {
            return objectMapper.writeValueAsString(notificationData);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing notification data", e);
            return null;
        }
    }
    
    private NotificationData deserializeNotificationData(String data) {
        try {
            return objectMapper.readValue(data, NotificationData.class);
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing notification data", e);
            return null;
        }
    }
    
    public static class NotificationData {
        public String transactionId;
        public String guestEmail;
        public String guestName;
        public java.time.LocalDate checkInDate;
        public java.time.LocalDate checkOutDate;
        public Double totalPrice;
        public List<NotificationMessage> messages;
    }
    
    public static class NotificationMessage {
        public String messageId;
        public String type; // EMAIL, SMS, PUSH
        public String recipient;
        public String subject;
        public String content;
        public String status; // QUEUED, SENT, FAILED, CANCELLED
        public String failureReason;
        public LocalDateTime sentAt;
        public LocalDateTime cancelledAt;
    }
}