package com.yen.springWarehouse.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/** Main page controller */

@Controller
@RequestMapping("/")
@Log4j2
public class IndexController {

    @GetMapping("/")
    public String main(Map<String, Object> map){

        log.info(">>> mainController, map = {}", map);
        return "main";
    }

}
