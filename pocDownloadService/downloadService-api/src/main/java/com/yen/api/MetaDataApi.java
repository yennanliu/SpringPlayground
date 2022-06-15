package com.yen.api;

import com.yen.data.bean.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/meta")
public interface MetaDataApi {

    @GetMapping("/getHistory")
    public Task[] getDownloadHistory();
}
