package com.yen.springWarehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.bean.DownloadStatus;
import com.yen.springWarehouse.service.DownloadStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    DownloadStatusService downloadStatusService;

    @GetMapping("/list")
    public String list(Map<String, Object> map, @RequestParam(value="pageNo", required=false, defaultValue="1") String pageNoStr) {

        int pageNo;
        // check pageNo
        pageNo = Integer.parseInt(pageNoStr);
        if(pageNo < 1){
            pageNo = 1;
        }
        log.info("pageNo = {}", pageNo);
        Page<DownloadStatus> page = new Page<>(pageNo,3);
        QueryWrapper<DownloadStatus> queryWrapper = new QueryWrapper<>();
        IPage<DownloadStatus> iPage = downloadStatusService.page(page,
                new LambdaQueryWrapper<DownloadStatus>()
                        .orderByAsc(DownloadStatus::getId)
        );

        log.info("iPage.total = {}, iPage.getPages = {} iPage = {}",  iPage.getTotal(), iPage.getPages(), iPage);
        map.put("page", iPage);

        return "download/list_download";
    }


}
