package com.yen.web.controller;

import com.yen.api.DownloadApi;
import com.yen.model.DownloadRequest;
import com.yen.model.DownloadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DownloadController implements DownloadApi {

    @Override
    public DownloadResponse createDownload(String exportType, Integer startDate, Integer endDate) {

        System.out.println(">>> exportType = " + exportType);
        System.out.println(">>> startDate = " + startDate);
        System.out.println(">>> endDate = " + endDate);
        System.out.println(">>> download run ...");
        return null;
    }

    @Override
    public void runDownload(String taskId) {
        System.out.println(">>> download task run ... id = " + taskId);
    }

}
