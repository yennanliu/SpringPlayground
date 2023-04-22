package com.yen.gulimall.ware.vo;

// https://youtu.be/L83Bxqy8UEE?t=263

import lombok.Data;

@Data
public class PurchaseItemDoneVo {

    private Long itemId;
    private Integer status;
    private String reason;
}
