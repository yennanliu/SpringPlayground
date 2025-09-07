package com.yen.HotelApp.entity;

import com.yen.HotelApp.transaction.TransactionState;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_records")
public class TransactionRecord {
    
    @Id
    private String transactionId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionState state;
    
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    @Column
    private LocalDateTime endTime;
    
    @Column(nullable = false)
    private LocalDateTime timeoutTime;
    
    @Column(nullable = false)
    private String participantIds;
    
    @Column(columnDefinition = "TEXT")
    private String transactionData;
    
    @Column
    private String initiatorId;
    
    @Column
    private String errorMessage;
    
    public TransactionRecord() {}
    
    public TransactionRecord(String transactionId, TransactionState state,
                           LocalDateTime startTime, LocalDateTime timeoutTime,
                           String participantIds, String transactionData) {
        this.transactionId = transactionId;
        this.state = state;
        this.startTime = startTime;
        this.timeoutTime = timeoutTime;
        this.participantIds = participantIds;
        this.transactionData = transactionData;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public TransactionState getState() {
        return state;
    }
    
    public void setState(TransactionState state) {
        this.state = state;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public LocalDateTime getTimeoutTime() {
        return timeoutTime;
    }
    
    public void setTimeoutTime(LocalDateTime timeoutTime) {
        this.timeoutTime = timeoutTime;
    }
    
    public String getParticipantIds() {
        return participantIds;
    }
    
    public void setParticipantIds(String participantIds) {
        this.participantIds = participantIds;
    }
    
    public String getTransactionData() {
        return transactionData;
    }
    
    public void setTransactionData(String transactionData) {
        this.transactionData = transactionData;
    }
    
    public String getInitiatorId() {
        return initiatorId;
    }
    
    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}