package com.yen.web.controller;

import com.yen.api.HistoryApi;
import com.yen.data.bean.History;
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

        Task t1 = new Task(001, new Integer[]{1,2,3}, new String[]{"colA", "colB"},"trade", 200, 300, "running");
        Task t2 = new Task(001, new Integer[]{4,5}, new String[]{"colC", "colD"},"deposit", 500, 600, "completed");

        ArrayList tasks = new ArrayList();
        tasks.add(t1);
        tasks.add(t2);

        // TODO : fix to use History as return type
        //History history = new History();
        //history.setTasks(tasks);

        return tasks;
    }

}
