package com.yen.springWarehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yen.springWarehouse.bean.DownloadStatus;

import java.util.List;

public interface DownloadStatusMapper extends BaseMapper<DownloadStatus> {

    List<DownloadStatus> getAllDownloadStatus();
}
