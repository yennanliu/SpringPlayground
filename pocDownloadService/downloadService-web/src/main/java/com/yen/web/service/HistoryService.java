package com.yen.web.service;

import com.yen.data.bean.Task;
import org.springframework.beans.factory.annotation.Autowired;

public interface HistoryService {

    public void addTask(Task task);

    public void listTask();
}
