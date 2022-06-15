package com.yen.api;

import com.yen.model.DownloadRequest;
import com.yen.model.DownloadResponse;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/task")
public interface DownloadApi {

    // TODO : fix return type
    @PostMapping("/create")
    public DownloadResponse createDownload(@RequestBody DownloadRequest request);

}
