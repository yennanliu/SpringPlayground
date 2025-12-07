package com.yen.ChatAppV2.dto;

import com.yen.ChatAppV2.model.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    @NotBlank
    private String channelId;

    @NotNull
    private Long senderId;

    @NotBlank
    @Size(max = 5000)
    private String content;

    private MessageType messageType = MessageType.TEXT;
}
