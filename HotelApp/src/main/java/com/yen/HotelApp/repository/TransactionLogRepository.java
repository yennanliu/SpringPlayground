package com.yen.HotelApp.repository;

import com.yen.HotelApp.entity.TransactionLogEntry;
import com.yen.HotelApp.transaction.TransactionPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLogEntry, Long> {
    
    List<TransactionLogEntry> findByTransactionIdOrderByTimestampAsc(String transactionId);
    
    List<TransactionLogEntry> findByTransactionIdAndParticipantId(String transactionId, String participantId);
    
    List<TransactionLogEntry> findByTransactionIdAndPhase(String transactionId, TransactionPhase phase);
    
    @Query("SELECT t FROM TransactionLogEntry t WHERE t.timestamp >= :since ORDER BY t.timestamp DESC")
    List<TransactionLogEntry> findRecentEntries(@Param("since") LocalDateTime since);
    
    @Query("SELECT DISTINCT t.transactionId FROM TransactionLogEntry t WHERE t.participantId = :participantId")
    List<String> findTransactionIdsByParticipant(@Param("participantId") String participantId);
    
    void deleteByTransactionId(String transactionId);
}