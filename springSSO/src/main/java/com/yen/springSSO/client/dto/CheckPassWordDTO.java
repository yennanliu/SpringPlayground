package com.yen.springSSO.client.dto;

// book p.30

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckPassWordDTO {

    private String userName;
}
