package com.yen.gulimall.ware.vo;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;

// https://youtu.be/L83Bxqy8UEE?t=235

@Data
public class PurchaseDoneVo {

    @NotNull
    private Long id; // purchase order id

    private List<PurchaseItemDoneVo> items;
}
