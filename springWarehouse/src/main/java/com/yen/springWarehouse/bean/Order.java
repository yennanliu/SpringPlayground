package com.yen.springWarehouse.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("orders")
public class Order implements Serializable {

  private static final long serialVersionUID = 58533333369667604L;

  // https://www.cnblogs.com/mark5/p/14268122.html
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;

  @TableField("merchant_id")
  private Integer merchantId;

  @TableField("product_id")
  private Integer productId;

  @TableField("amount")
  private Integer amount;

  @TableField("status")
  private String status;

  @TableField("create_time")
  private LocalDateTime createTime;

  @TableField("update_time")
  private LocalDateTime updateTime;
}
