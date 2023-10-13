package com.yen.springWarehouse.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order")
public class Order implements Serializable {

    private static final long serialVersionUID = 58533333369667604L;

    @TableId(type = IdType.AUTO)
    private int id;

    @TableField("merchant_id")
    private int merchantId;

    @TableField("product_id")
    private int productId;

    @TableField("amount")
    private int amount;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
