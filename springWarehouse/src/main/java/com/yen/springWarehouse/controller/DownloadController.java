package com.yen.springWarehouse.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/download")
public class DownloadController {

    @GetMapping("/list")
    public String getReport(Map<String, Object> map) {

        //map.put("Merchant", new Merchant());
        return "download/list_download";
    }

}
