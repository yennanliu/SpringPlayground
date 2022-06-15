package com.yen.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/task")
public interface DownloadApi {

    // TODO : fix return type
    @PostMapping("/run")
    public void runDownload();

}
