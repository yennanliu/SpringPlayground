package com.yen.springWarehouse.service.impl;

import com.yen.springWarehouse.mapper.DownloadStatusMapper;
import com.yen.springWarehouse.service.DownloadService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DownloadServiceTest {

    @Autowired
    DownloadService downloadService;

    @Autowired
    DownloadStatusMapper downloadStatusMapper;

    @Test
    public void testGetDownloadStatus(){

        System.out.println(downloadStatusMapper.getAllDownloadStatus());
        System.out.println(downloadStatusMapper.getAllDownloadStatus().size());
    }

}