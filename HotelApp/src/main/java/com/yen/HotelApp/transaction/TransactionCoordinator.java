package com.yen.HotelApp.transaction;

import java.util.List;

public interface TransactionCoordinator {
    
    String beginTransaction(List<TransactionParticipant> participants, Object transactionData);
    
    TransactionResult executeTransaction(String transactionId);
    
    void abortTransaction(String transactionId);
    
    TransactionContext getTransactionContext(String transactionId);
    
    List<String> getActiveTransactions();
    
    void recoverTransaction(String transactionId);
    
    boolean isTransactionActive(String transactionId);
    
    void cleanupCompletedTransactions();
}