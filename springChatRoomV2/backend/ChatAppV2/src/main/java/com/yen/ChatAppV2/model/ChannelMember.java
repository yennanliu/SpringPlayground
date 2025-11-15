package com.yen.ChatAppV2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "channel_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ChannelMemberId.class)
public class ChannelMember {

    @Id
    @Column(name = "channel_id")
    private Long channelId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "last_read_at")
    private LocalDateTime lastReadAt;

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }
}
