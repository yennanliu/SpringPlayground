package com.yen.controller;

import com.yen.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/report")
    public String[] getFields(@RequestParam("name") String name){

        System.out.println(">>> name = " + name);

        String[] fields = reportService.getReportField(name);
        System.out.println(">>> fields = " + fields);

        return fields;
    }

}
