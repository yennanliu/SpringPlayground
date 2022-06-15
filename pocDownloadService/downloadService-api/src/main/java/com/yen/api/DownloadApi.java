package com.yen.api;

import com.yen.model.DownloadRequest;
import com.yen.model.DownloadResponse;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/task")
public interface DownloadApi {

    @PostMapping("/create")
    // TODO : fix param
    //public DownloadResponse createDownload(@RequestBody DownloadRequest request);
    public DownloadResponse createDownload(@RequestParam(name = "exportType") String exportType,
                                           @RequestParam(name = "startDate") Integer startDate,
                                           @RequestParam(name = "endDate") Integer endDate
                                           );

    @PostMapping("/run")
    public void runDownload(@RequestParam(name="taskId") String taskId);

}
