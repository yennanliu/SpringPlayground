package com.yen.web.controller;

import com.yen.api.HistoryApi;
import com.yen.data.bean.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RestController
public class HistoryController implements HistoryApi {
    @Override
    public ArrayList<Task> getAllHistory() {
        System.out.println(">>> get all history ...");

        Task t1 = new Task(001, 100, 200, "trade", "running");
        Task t2 = new Task(002, 101, 101, "trade", "completed");

        ArrayList tasks = new ArrayList();
        tasks.add(t1);
        tasks.add(t2);

        System.out.println(">>> tasks = " + tasks.toArray().toString());

        return tasks;
    }

}
