package com.yen.web;

import com.yen.api.DownloadApi;
import com.yen.model.DownloadRequest;
import com.yen.model.DownloadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class DownloadController implements DownloadApi {
    
    @Override
    public DownloadResponse createDownload(DownloadRequest request) {
        return null;
    }

}
