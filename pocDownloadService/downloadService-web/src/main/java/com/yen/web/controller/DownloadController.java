package com.yen.web.controller;

import com.yen.api.DownloadApi;
import com.yen.data.bean.Task;
import com.yen.model.DownloadRequest;
import com.yen.model.DownloadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DownloadController implements DownloadApi {

    @Override
    public DownloadResponse createDownload(Integer[] userList, String[] reportField, String exportType, Integer startDate, Integer endDate) {

        System.out.println(">>> userList = " + userList);
        System.out.println(">>> reportField = " + reportField);
        System.out.println(">>> exportType = " + exportType);
        System.out.println(">>> startDate = " + startDate);
        System.out.println(">>> endDate = " + endDate);
        System.out.println(">>> download run ...");

        Task task = new Task();
        task.setUserList(userList);
        task.setReportField(reportField);
        task.setExportType(exportType);
        task.setStartTime(startDate);
        task.setEndTime(endDate);
        task.setStatus(null);

        System.out.println(">>> task = " + task);

        // TODO : fix return value
        return null;
    }

    @Override
    public void runDownload(String taskId) {

        // TODO : get taskID from DB, when taskID exist, run download
        System.out.println(">>> download task run ... id = " + taskId);
    }

}
