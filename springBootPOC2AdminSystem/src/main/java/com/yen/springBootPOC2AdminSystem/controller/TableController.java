package com.yen.springBootPOC2AdminSystem.controller;

// https://www.youtube.com/watch?v=59dkU-lunaA&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=46

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TableController {

    @GetMapping("/basic_table")
    public String basic_table(){

        return "table/basic_table"; // resources/templates/table/basic_table.html
    }

    @GetMapping("/dynamic_table")
    public String dynamic_table(){

        return "table/dynamic_table"; // resources/templates/table/dynamic_table.html
    }

    @GetMapping("/responsive_table")
    public String responsive_table(){

        return "table/responsive_table"; // resources/templates/table/responsive_table.html
    }

    @GetMapping("/editable_table")
    public String editable_table(){

        return "table/editable_table"; // resources/templates/table/editable_table.html
    }

}
