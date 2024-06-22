package com.yen.springWarehouse.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("download_status")
public class DownloadStatus implements Serializable {

  private static final long serialVersionUID = 234543434343815L;
  @TableField("download_url")
  String downloadUrl;
  @TableField("status")
  String status;
  @TableId(type = IdType.AUTO)
  private int id;
  @TableField("create_time")
  private Date createTime;

  @TableField("complete_time")
  private Date completeTime;
}
