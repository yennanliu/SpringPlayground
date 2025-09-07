package com.yen.HotelApp.transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionContext {
    
    private String transactionId;
    private TransactionState state;
    private List<String> participantIds;
    private LocalDateTime startTime;
    private LocalDateTime timeoutTime;
    private Map<String, ParticipantState> participantStates;
    private Object transactionData;
    private String initiatorId;
    private int timeoutSeconds;
    
    public TransactionContext() {
        this.participantStates = new ConcurrentHashMap<>();
        this.startTime = LocalDateTime.now();
        this.state = TransactionState.ACTIVE;
        this.timeoutSeconds = 300; // 5 minutes default
    }
    
    public TransactionContext(String transactionId, List<String> participantIds, Object transactionData) {
        this();
        this.transactionId = transactionId;
        this.participantIds = participantIds;
        this.transactionData = transactionData;
        this.timeoutTime = this.startTime.plusSeconds(timeoutSeconds);
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
    
    public List<String> getParticipantIds() {
        return participantIds;
    }
    
    public void setParticipantIds(List<String> participantIds) {
        this.participantIds = participantIds;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getTimeoutTime() {
        return timeoutTime;
    }
    
    public void setTimeoutTime(LocalDateTime timeoutTime) {
        this.timeoutTime = timeoutTime;
    }
    
    public Map<String, ParticipantState> getParticipantStates() {
        return participantStates;
    }
    
    public void setParticipantStates(Map<String, ParticipantState> participantStates) {
        this.participantStates = participantStates;
    }
    
    public Object getTransactionData() {
        return transactionData;
    }
    
    public void setTransactionData(Object transactionData) {
        this.transactionData = transactionData;
    }
    
    public String getInitiatorId() {
        return initiatorId;
    }
    
    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }
    
    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }
    
    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
        if (this.startTime != null) {
            this.timeoutTime = this.startTime.plusSeconds(timeoutSeconds);
        }
    }
    
    public void updateParticipantState(String participantId, ParticipantState state) {
        this.participantStates.put(participantId, state);
    }
    
    public ParticipantState getParticipantState(String participantId) {
        return this.participantStates.get(participantId);
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(timeoutTime);
    }
    
    public boolean allParticipantsInState(ParticipantState state) {
        if (participantIds == null || participantIds.isEmpty()) {
            return false;
        }
        
        for (String participantId : participantIds) {
            if (!state.equals(participantStates.get(participantId))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean anyParticipantInState(ParticipantState state) {
        return participantStates.values().contains(state);
    }
}