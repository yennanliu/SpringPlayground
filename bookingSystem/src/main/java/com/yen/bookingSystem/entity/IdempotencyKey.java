package com.yen.bookingSystem.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_key")
public class IdempotencyKey {

    @Id
    @Column(name = "\"key\"", length = 100)
    private String key;

    @Column(columnDefinition = "TEXT")
    private String response;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public IdempotencyKey() {}

    public IdempotencyKey(String key, String response) {
        this.key = key;
        this.response = response;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
