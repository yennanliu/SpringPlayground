package com.yen.HotelApp.repository;

import com.yen.HotelApp.entity.PreparedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PreparedTransactionRepository extends JpaRepository<PreparedTransaction, Long> {
    
    List<PreparedTransaction> findByTransactionId(String transactionId);
    
    Optional<PreparedTransaction> findByTransactionIdAndParticipantId(String transactionId, String participantId);
    
    List<PreparedTransaction> findByParticipantId(String participantId);
    
    @Query("SELECT p FROM PreparedTransaction p WHERE p.expiresAt < :now")
    List<PreparedTransaction> findExpiredTransactions(@Param("now") LocalDateTime now);
    
    void deleteByTransactionId(String transactionId);
    
    void deleteByTransactionIdAndParticipantId(String transactionId, String participantId);
    
    @Query("SELECT COUNT(p) FROM PreparedTransaction p WHERE p.expiresAt > :now")
    long countActivePreparedTransactions(@Param("now") LocalDateTime now);
}