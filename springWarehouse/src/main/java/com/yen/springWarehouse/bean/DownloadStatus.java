package com.yen.springWarehouse.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("download_status")
public class DownloadStatus implements Serializable {

        private static final long serialVersionUID = 234543434343815L;

        @TableId(type = IdType.AUTO)
        private int id;

        @TableField("download_url")
        String downloadUrl;

        @TableField("status")
        String status;

        @TableField("create_time")
        private Date createTime;

        @TableField("complete_time")
        private Date completeTime;
}
