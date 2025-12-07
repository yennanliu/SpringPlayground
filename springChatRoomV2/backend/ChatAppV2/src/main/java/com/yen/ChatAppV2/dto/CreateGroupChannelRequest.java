package com.yen.ChatAppV2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupChannelRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    private Long creatorId; // Optional - will use default if not provided

    private List<Long> memberIds;
}
