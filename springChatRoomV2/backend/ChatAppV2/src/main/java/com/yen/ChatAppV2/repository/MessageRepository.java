package com.yen.ChatAppV2.repository;

import com.yen.ChatAppV2.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByChannelIdOrderByCreatedAtDesc(String channelId, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.channelId = :channelId " +
           "AND m.createdAt > :since ORDER BY m.createdAt ASC")
    List<Message> findRecentMessages(@Param("channelId") String channelId,
                                    @Param("since") LocalDateTime since);

    Long countByChannelIdAndCreatedAtAfter(String channelId, LocalDateTime timestamp);

    @Query("SELECT m FROM Message m WHERE LOWER(m.content) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY m.createdAt DESC")
    Page<Message> searchMessages(@Param("query") String query, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.channelId = :channelId " +
           "AND LOWER(m.content) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY m.createdAt DESC")
    Page<Message> searchMessagesByChannel(@Param("query") String query,
                                          @Param("channelId") String channelId,
                                          Pageable pageable);
}
