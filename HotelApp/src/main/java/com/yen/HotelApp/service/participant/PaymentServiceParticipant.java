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
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class PaymentServiceParticipant implements TransactionParticipant {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceParticipant.class);
    private static final String PARTICIPANT_ID = "PAYMENT_SERVICE";
    private static final Random random = new Random();
    
    @Autowired
    private PreparedTransactionRepository preparedTransactionRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    @Transactional
    public ParticipantState prepare(String transactionId, TransactionContext context) {
        logger.info("Preparing payment for transaction: {}", transactionId);
        
        try {
            BookingRequest request = (BookingRequest) context.getTransactionData();
            
            if (request.getTotalPrice() == null || request.getTotalPrice() <= 0) {
                logger.warn("Invalid payment amount for transaction: {}", transactionId);
                return ParticipantState.ABORTED;
            }
            
            if (request.getPaymentMethod() == null || request.getPaymentMethod().trim().isEmpty()) {
                logger.warn("Missing payment method for transaction: {}", transactionId);
                return ParticipantState.ABORTED;
            }
            
            Thread.sleep(random.nextInt(1000) + 500);
            
            if (random.nextDouble() < 0.1) {
                logger.warn("Payment authorization failed for transaction: {}", transactionId);
                return ParticipantState.ABORTED;
            }
            
            PaymentData paymentData = new PaymentData();
            paymentData.transactionId = transactionId;
            paymentData.amount = request.getTotalPrice();
            paymentData.paymentMethod = request.getPaymentMethod();
            paymentData.paymentToken = request.getPaymentToken();
            paymentData.authorizationId = "AUTH_" + UUID.randomUUID().toString();
            paymentData.status = "AUTHORIZED";
            
            PreparedTransaction preparedTx = new PreparedTransaction(
                transactionId,
                PARTICIPANT_ID,
                serializePaymentData(paymentData),
                LocalDateTime.now().plusSeconds(context.getTimeoutSeconds())
            );
            preparedTransactionRepository.save(preparedTx);
            
            logger.info("Payment authorized for transaction: {} with auth ID: {}", 
                       transactionId, paymentData.authorizationId);
            return ParticipantState.PREPARED;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Payment preparation interrupted for transaction: {}", transactionId);
            return ParticipantState.ABORTED;
        } catch (Exception e) {
            logger.error("Error preparing payment for transaction: {}", transactionId, e);
            return ParticipantState.ABORTED;
        }
    }
    
    @Override
    @Transactional
    public void commit(String transactionId) {
        logger.info("Committing payment for transaction: {}", transactionId);
        
        try {
            Optional<PreparedTransaction> preparedTxOpt = 
                preparedTransactionRepository.findByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
            if (preparedTxOpt.isEmpty()) {
                logger.warn("No prepared payment found for commit: {}", transactionId);
                return;
            }
            
            PreparedTransaction preparedTx = preparedTxOpt.get();
            PaymentData paymentData = deserializePaymentData(preparedTx.getPreparedData());
            
            Thread.sleep(random.nextInt(500) + 200);
            
            paymentData.status = "CAPTURED";
            paymentData.captureId = "CAPTURE_" + UUID.randomUUID().toString();
            paymentData.capturedAt = LocalDateTime.now();
            
            logger.info("Payment captured for transaction: {} with capture ID: {}", 
                       transactionId, paymentData.captureId);
            
            preparedTransactionRepository.deleteByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Payment commit interrupted for transaction: {}", transactionId);
        } catch (Exception e) {
            logger.error("Error committing payment for transaction: {}", transactionId, e);
        }
    }
    
    @Override
    @Transactional
    public void rollback(String transactionId) {
        logger.info("Rolling back payment for transaction: {}", transactionId);
        
        try {
            Optional<PreparedTransaction> preparedTxOpt = 
                preparedTransactionRepository.findByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
            if (preparedTxOpt.isEmpty()) {
                logger.warn("No prepared payment found for rollback: {}", transactionId);
                return;
            }
            
            PreparedTransaction preparedTx = preparedTxOpt.get();
            PaymentData paymentData = deserializePaymentData(preparedTx.getPreparedData());
            
            Thread.sleep(random.nextInt(300) + 100);
            
            paymentData.status = "VOIDED";
            paymentData.voidId = "VOID_" + UUID.randomUUID().toString();
            paymentData.voidedAt = LocalDateTime.now();
            
            logger.info("Payment authorization voided for transaction: {} with void ID: {}", 
                       transactionId, paymentData.voidId);
            
            preparedTransactionRepository.deleteByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Payment rollback interrupted for transaction: {}", transactionId);
        } catch (Exception e) {
            logger.error("Error rolling back payment for transaction: {}", transactionId, e);
        }
    }
    
    @Override
    public String getParticipantId() {
        return PARTICIPANT_ID;
    }
    
    @Override
    public boolean canTimeout() {
        return true;
    }
    
    @Override
    public int getTimeoutSeconds() {
        return 180; // 3 minutes (shorter for payment)
    }
    
    private String serializePaymentData(PaymentData paymentData) {
        try {
            return objectMapper.writeValueAsString(paymentData);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing payment data", e);
            return null;
        }
    }
    
    private PaymentData deserializePaymentData(String data) {
        try {
            return objectMapper.readValue(data, PaymentData.class);
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing payment data", e);
            return null;
        }
    }
    
    public static class PaymentData {
        public String transactionId;
        public Double amount;
        public String paymentMethod;
        public String paymentToken;
        public String authorizationId;
        public String captureId;
        public String voidId;
        public String status;
        public LocalDateTime capturedAt;
        public LocalDateTime voidedAt;
    }
}