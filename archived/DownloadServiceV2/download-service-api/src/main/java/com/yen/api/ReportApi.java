package com.yen.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface ReportApi {

    @GetMapping("/report")
    public String[] getFields(@RequestParam("name") String name);
}
