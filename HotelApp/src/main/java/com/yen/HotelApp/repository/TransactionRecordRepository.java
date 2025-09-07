package com.yen.HotelApp.repository;

import com.yen.HotelApp.entity.TransactionRecord;
import com.yen.HotelApp.transaction.TransactionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, String> {
    
    List<TransactionRecord> findByState(TransactionState state);
    
    List<TransactionRecord> findByStateIn(List<TransactionState> states);
    
    @Query("SELECT t FROM TransactionRecord t WHERE t.timeoutTime < :now AND t.state IN :activeStates")
    List<TransactionRecord> findExpiredTransactions(@Param("now") LocalDateTime now, 
                                                    @Param("activeStates") List<TransactionState> activeStates);
    
    @Query("SELECT t FROM TransactionRecord t WHERE t.endTime IS NOT NULL AND t.endTime < :cutoff")
    List<TransactionRecord> findCompletedTransactionsBefore(@Param("cutoff") LocalDateTime cutoff);
    
    @Query("SELECT t FROM TransactionRecord t WHERE t.participantIds LIKE %:participantId%")
    List<TransactionRecord> findTransactionsWithParticipant(@Param("participantId") String participantId);
    
    @Query("SELECT COUNT(t) FROM TransactionRecord t WHERE t.state IN :activeStates")
    long countActiveTransactions(@Param("activeStates") List<TransactionState> activeStates);
}