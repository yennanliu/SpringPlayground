package com.yen.springWarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadDto {
        String reportId;
        String downloadUrl;
        String status;
        private Date createTime;
        private Date completeTime;
}
