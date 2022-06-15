package com.yen.web.controller;

import com.yen.api.HistoryApi;
import com.yen.data.bean.Task;

public class HistoryController implements HistoryApi {
    @Override
    public Task[] getAllHistory() {
        System.out.println(">>> get all history ...");
        return new Task[0];
    }
}
