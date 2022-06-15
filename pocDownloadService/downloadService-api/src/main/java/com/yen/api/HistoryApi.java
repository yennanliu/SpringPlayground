package com.yen.api;

import com.yen.data.bean.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@RequestMapping(value = "/history")
public interface HistoryApi {

    @GetMapping("/list")
    public <T> ArrayList<T> getAllHistory();

}
