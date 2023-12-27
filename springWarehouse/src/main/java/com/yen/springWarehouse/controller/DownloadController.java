package com.yen.springWarehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.bean.DownloadStatus;
import com.yen.springWarehouse.mapper.DownloadStatusMapper;
import com.yen.springWarehouse.service.DownloadStatusService;
import com.yen.springWarehouse.util.DateTimeUtils;
import com.yen.springWarehouse.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    DownloadStatusService downloadStatusService;

    @Autowired
    DownloadStatusMapper downloadStatusMapper;

    String userDirectory = new File("").getAbsolutePath();
    //final String prefix = userDirectory + "/src/main/resources/report/";
    final String prefix = userDirectory + "/src/main/resources/";

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

    @GetMapping("/create_report")
    public String createDownload() throws InterruptedException {

        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        String timestamp = dateTimeUtils.getCurrentDateYYYYMMDDHHMMSS();
        FileUtil fileUtil = new FileUtil();

//        log.info("--------------------------------------------\n");
//        log.info(">>>>>>>>> SLEEP 10 SECONDS >>>>>>>>>\n");
//        Thread.sleep(10000);
//        log.info("--------------------------------------------\n");

        // create report
        String fileName = timestamp + "_report.json";
        Map<String, Object> map = new HashMap<>();
        map.put("name", "king");
        map.put("age", 17);
        // save file to local // TODO : save it to remote file system (e.g. S3)
        Boolean result = fileUtil.writeJsonFile(map, prefix + fileName);
        //Boolean result = fileUtil.writeJsonFile(map, fileName);
        if (result) {
            DownloadStatus downloadStatus = new DownloadStatus();
            downloadStatus.setStatus("completed");
            downloadStatus.setDownloadUrl(fileName);
            downloadStatus.setCreateTime(new Date());
            log.info("save File OK");
            // save to DB
            downloadStatusService.save(downloadStatus);
        } else {
            log.info("save File failed");
        }
        return "download/success";
    }

    // TODO : fix : make GET request with report id
    @GetMapping("/download_report")
    public ResponseEntity<Resource> downloadFile(String url) throws IOException {

        log.info(">>> (ResponseEntity<Resource> downloadFile) url = " + url);
        //List<DownloadStatus> downloadStatusList = downloadStatusMapper.getAllDownloadStatus();
        String downloadUrl = url;
        log.info(">>> downloadUrl = " + downloadUrl);

        Resource resource = null;
        try{
            File file = new File("src/main/resources/" + downloadUrl);
            resource = new InputStreamResource(new FileInputStream(file));
        }catch (Exception e){
            log.error("download failed ");
            e.printStackTrace();
        }

        // TODO : fix why can't read file from downloadUrl
//        ClassPathResource resource = new ClassPathResource(String.valueOf(downloadUrl));
//        //ClassPathResource resource = new ClassPathResource("demo_report.json");
//        log.info(resource.getPath() + String.valueOf(resource.exists()) + String.valueOf(resource.getURL()));

        HttpHeaders headers = new HttpHeaders();
        //headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".format("dummy.json"));
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".format(downloadUrl));

        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

}
