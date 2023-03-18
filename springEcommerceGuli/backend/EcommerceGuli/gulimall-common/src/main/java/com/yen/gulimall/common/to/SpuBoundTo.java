package com.yen.gulimall.common.to;

import lombok.Data;

import java.math.BigDecimal;

// To:  DTO (Data Transfer Object)
// 傳輸用的物件，假設今天我的程式像資料庫提取了PO資料物件，我必須將我的資料傳往其他系統或是服務時則可以用DTO進行再包裝，通常DTO的資訊都會比PO少，因為沒有必要將全部的資料傳輸出去。
// https://youtu.be/2Fgtxnc9ehQ?t=398
@Data
public class SpuBoundTo {

    private Long spuId;
    private BigDecimal growBounds;
    private BigDecimal buyBounds;
}
