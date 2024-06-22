package com.yen.springWarehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.springWarehouse.bean.DownloadStatus;
import com.yen.springWarehouse.mapper.DownloadStatusMapper;
import com.yen.springWarehouse.service.DownloadStatusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DownloadStatusServiceImpl extends ServiceImpl<DownloadStatusMapper, DownloadStatus>
    implements DownloadStatusService {}
