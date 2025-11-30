package com.yen.ChatAppV2.dto;

import com.yen.ChatAppV2.model.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDTO {
    private Long id;
    private ChannelType channelType;
    private String name;
    private Integer memberCount;
    private LocalDateTime createdAt;
}
