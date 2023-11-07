package com.yen.springWarehouse.controller;

import com.yen.springWarehouse.bean.Merchant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/report")
public class ReportController {

    @GetMapping("/report_page")
    public String getReport(Map<String, Object> map) {

        //map.put("Merchant", new Merchant());
        return "report/report";
    }

}
