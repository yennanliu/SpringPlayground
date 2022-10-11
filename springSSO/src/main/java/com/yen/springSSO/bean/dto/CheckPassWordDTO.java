package com.yen.springSSO.bean.dto;

// book p.30

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckPassWordDTO {

    private String userName;
}
