package com.yen.ChatAppV2.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class ChannelTest {

    @Test
    void testChannelCreation() {
        // Given
        Channel channel = new Channel();
        channel.setId(1L);
        channel.setChannelType(ChannelType.GROUP);
        channel.setName("General");
        channel.setCreatedBy(1L);

        // Then
        assertThat(channel.getId()).isEqualTo(1L);
        assertThat(channel.getChannelType()).isEqualTo(ChannelType.GROUP);
        assertThat(channel.getName()).isEqualTo("General");
        assertThat(channel.getCreatedBy()).isEqualTo(1L);
    }

    @Test
    void testChannelPrePersist() {
        // Given
        Channel channel = new Channel();
        channel.setChannelType(ChannelType.GROUP);
        channel.setName("General");

        // When
        channel.onCreate();

        // Then
        assertThat(channel.getCreatedAt()).isNotNull();
        assertThat(channel.getCreatedAt()).isBefore(LocalDateTime.now().plusSeconds(1));
    }

    @Test
    void testDirectChannelCreation() {
        // Given
        Channel channel = new Channel();
        channel.setChannelType(ChannelType.DIRECT);
        channel.setCreatedBy(1L);

        // Then
        assertThat(channel.getChannelType()).isEqualTo(ChannelType.DIRECT);
        assertThat(channel.getName()).isNull(); // Direct channels don't need names
    }

    @Test
    void testChannelAllArgsConstructor() {
        // When
        LocalDateTime now = LocalDateTime.now();
        Channel channel = new Channel(1L, ChannelType.GROUP, "General", 1L, now);

        // Then
        assertThat(channel.getId()).isEqualTo(1L);
        assertThat(channel.getChannelType()).isEqualTo(ChannelType.GROUP);
        assertThat(channel.getName()).isEqualTo("General");
        assertThat(channel.getCreatedBy()).isEqualTo(1L);
        assertThat(channel.getCreatedAt()).isEqualTo(now);
    }
}
