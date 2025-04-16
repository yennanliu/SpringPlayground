package com.example.chatapp.dto;

import com.example.chatapp.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private Message.MessageType type;
    private Long senderId;
    private String senderUsername;
    private Long channelId;
    private Long recipientId;
} 