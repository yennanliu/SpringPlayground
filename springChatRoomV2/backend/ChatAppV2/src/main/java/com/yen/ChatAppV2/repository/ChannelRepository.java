package com.yen.ChatAppV2.repository;

import com.yen.ChatAppV2.model.Channel;
import com.yen.ChatAppV2.model.ChannelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findByChannelType(ChannelType channelType);

    @Query("SELECT c FROM Channel c JOIN ChannelMember cm ON c.id = cm.channelId " +
           "WHERE cm.userId = :userId")
    List<Channel> findChannelsByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Channel c WHERE c.channelType = 'DIRECT' " +
           "AND EXISTS (SELECT 1 FROM ChannelMember cm1 WHERE cm1.channelId = c.id AND cm1.userId = :userId1) " +
           "AND EXISTS (SELECT 1 FROM ChannelMember cm2 WHERE cm2.channelId = c.id AND cm2.userId = :userId2)")
    Optional<Channel> findDirectChannel(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}
