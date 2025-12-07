package com.yen.ChatAppV2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadReceiptEvent {
    private Long userId;
    private String channelId;
    private Long messageId;
}
