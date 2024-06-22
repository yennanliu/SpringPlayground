package com.yen.springWarehouse.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("merchant")
public class Merchant implements Serializable {

  private static final long serialVersionUID = 234345356533250815L;

  @TableId(type = IdType.AUTO)
  private int id;

  @TableField("name")
  private String name;

  @TableField("city")
  private String city;

  @TableField("type")
  private String type;

  @TableField("status")
  private String status;
}
