package com.yen.ChatAppV2.repository;

import com.yen.ChatAppV2.model.ChannelMember;
import com.yen.ChatAppV2.model.ChannelMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelMemberRepository extends JpaRepository<ChannelMember, ChannelMemberId> {
    List<ChannelMember> findByChannelId(String channelId);
    List<ChannelMember> findByUserId(Long userId);
    boolean existsByChannelIdAndUserId(String channelId, Long userId);
    void deleteByChannelIdAndUserId(String channelId, Long userId);
}
