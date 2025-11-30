package com.yen.ChatAppV2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypingEvent {
    private Long userId;
    private String username;
    private boolean isTyping;
}
