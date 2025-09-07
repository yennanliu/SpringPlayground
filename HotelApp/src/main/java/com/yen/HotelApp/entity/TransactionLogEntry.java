package com.yen.HotelApp.entity;

import com.yen.HotelApp.transaction.ParticipantState;
import com.yen.HotelApp.transaction.TransactionPhase;
import com.yen.HotelApp.transaction.TransactionState;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_log")
public class TransactionLogEntry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String transactionId;
    
    @Column(nullable = false)
    private String participantId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionPhase phase;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipantState participantState;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionState transactionState;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(columnDefinition = "TEXT")
    private String payload;
    
    @Column
    private String errorMessage;
    
    public TransactionLogEntry() {
        this.timestamp = LocalDateTime.now();
    }
    
    public TransactionLogEntry(String transactionId, String participantId, 
                              TransactionPhase phase, ParticipantState participantState,
                              TransactionState transactionState) {
        this();
        this.transactionId = transactionId;
        this.participantId = participantId;
        this.phase = phase;
        this.participantState = participantState;
        this.transactionState = transactionState;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getParticipantId() {
        return participantId;
    }
    
    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }
    
    public TransactionPhase getPhase() {
        return phase;
    }
    
    public void setPhase(TransactionPhase phase) {
        this.phase = phase;
    }
    
    public ParticipantState getParticipantState() {
        return participantState;
    }
    
    public void setParticipantState(ParticipantState participantState) {
        this.participantState = participantState;
    }
    
    public TransactionState getTransactionState() {
        return transactionState;
    }
    
    public void setTransactionState(TransactionState transactionState) {
        this.transactionState = transactionState;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getPayload() {
        return payload;
    }
    
    public void setPayload(String payload) {
        this.payload = payload;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}