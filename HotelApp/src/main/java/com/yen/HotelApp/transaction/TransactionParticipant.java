package com.yen.HotelApp.transaction;

public interface TransactionParticipant {
    
    ParticipantState prepare(String transactionId, TransactionContext context);
    
    void commit(String transactionId);
    
    void rollback(String transactionId);
    
    String getParticipantId();
    
    boolean canTimeout();
    
    int getTimeoutSeconds();
}