package com.yen.ChatAppV2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMemberId implements Serializable {
    private Long channelId;
    private Long userId;
}
