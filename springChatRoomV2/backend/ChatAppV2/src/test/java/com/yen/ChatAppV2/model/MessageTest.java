package com.yen.ChatAppV2.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class MessageTest {

    @Test
    void testMessageCreation() {
        // Given
        Message message = new Message();
        message.setId(1L);
        message.setChannelId(1L);
        message.setSenderId(1L);
        message.setContent("Hello World");
        message.setMessageType(MessageType.TEXT);

        // Then
        assertThat(message.getId()).isEqualTo(1L);
        assertThat(message.getChannelId()).isEqualTo(1L);
        assertThat(message.getSenderId()).isEqualTo(1L);
        assertThat(message.getContent()).isEqualTo("Hello World");
        assertThat(message.getMessageType()).isEqualTo(MessageType.TEXT);
    }

    @Test
    void testMessagePrePersist() {
        // Given
        Message message = new Message();
        message.setChannelId(1L);
        message.setSenderId(1L);
        message.setContent("Test message");

        // When
        message.onCreate();

        // Then
        assertThat(message.getCreatedAt()).isNotNull();
        assertThat(message.getMessageType()).isEqualTo(MessageType.TEXT);
        assertThat(message.getCreatedAt()).isBefore(LocalDateTime.now().plusSeconds(1));
    }

    @Test
    void testImageMessageType() {
        // Given
        Message message = new Message();
        message.setChannelId(1L);
        message.setSenderId(1L);
        message.setContent("https://example.com/image.jpg");
        message.setMessageType(MessageType.IMAGE);

        // Then
        assertThat(message.getMessageType()).isEqualTo(MessageType.IMAGE);
    }

    @Test
    void testFileMessageType() {
        // Given
        Message message = new Message();
        message.setChannelId(1L);
        message.setSenderId(1L);
        message.setContent("https://example.com/file.pdf");
        message.setMessageType(MessageType.FILE);

        // Then
        assertThat(message.getMessageType()).isEqualTo(MessageType.FILE);
    }
}
