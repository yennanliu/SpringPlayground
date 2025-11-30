package com.yen.ChatAppV2.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDirectChannelRequest {

    @NotNull
    private Long userId1;

    @NotNull
    private Long userId2;
}
