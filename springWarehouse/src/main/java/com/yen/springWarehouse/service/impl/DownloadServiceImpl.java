package com.yen.springWarehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.springWarehouse.bean.DownloadStatus;
import com.yen.springWarehouse.bean.Merchant;
import com.yen.springWarehouse.mapper.DownloadStatusMapper;
import com.yen.springWarehouse.mapper.MerchantMapper;
import com.yen.springWarehouse.service.DownloadService;
import com.yen.springWarehouse.service.MerchantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DownloadServiceImpl extends ServiceImpl<DownloadStatusMapper, DownloadStatus> implements DownloadService {
}
