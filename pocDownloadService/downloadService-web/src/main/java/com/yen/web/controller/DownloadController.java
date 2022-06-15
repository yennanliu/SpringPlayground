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
    public DownloadResponse createDownload(DownloadRequest request) {
        System.out.println(">>> download run ...");
        return null;
    }

}
