package com.yen.web.controller;

import com.yen.api.HistoryApi;
import com.yen.data.bean.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HistoryController implements HistoryApi {
    @Override
    public Task[] getAllHistory() {
        System.out.println(">>> get all history ...");
        return new Task[0];
    }

}
