package com.yen.HotelApp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prepared_transactions")
public class PreparedTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String transactionId;
    
    @Column(nullable = false)
    private String participantId;
    
    @Column(columnDefinition = "TEXT")
    private String preparedData;
    
    @Column(nullable = false)
    private LocalDateTime preparedAt;
    
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    
    public PreparedTransaction() {
        this.preparedAt = LocalDateTime.now();
    }
    
    public PreparedTransaction(String transactionId, String participantId, 
                              String preparedData, LocalDateTime expiresAt) {
        this();
        this.transactionId = transactionId;
        this.participantId = participantId;
        this.preparedData = preparedData;
        this.expiresAt = expiresAt;
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
    
    public String getPreparedData() {
        return preparedData;
    }
    
    public void setPreparedData(String preparedData) {
        this.preparedData = preparedData;
    }
    
    public LocalDateTime getPreparedAt() {
        return preparedAt;
    }
    
    public void setPreparedAt(LocalDateTime preparedAt) {
        this.preparedAt = preparedAt;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}