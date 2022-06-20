package com.yen.web.service.impl;

import com.yen.data.bean.Task;
import com.yen.web.mapper.HistoryMapper2;
import com.yen.web.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    HistoryMapper2 historyMapper2;

    @Override
    public void addTask(Task task) {
        historyMapper2.insertTask(task);
    }

    @Override
    public void listTask() {

    }
}
