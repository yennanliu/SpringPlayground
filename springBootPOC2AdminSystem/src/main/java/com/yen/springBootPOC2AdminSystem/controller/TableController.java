package com.yen.springBootPOC2AdminSystem.controller;

// https://www.youtube.com/watch?v=59dkU-lunaA&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=46
// https://www.youtube.com/watch?v=PpheT7laE_8&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=47
// https://www.youtube.com/watch?v=TOwcNVQtniU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=56
// https://www.youtube.com/watch?v=njvVPhCFH6o&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=66
// https://www.youtube.com/watch?v=pzL68_zvqK4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=67

import com.yen.springBootPOC2AdminSystem.bean.User;
import com.yen.springBootPOC2AdminSystem.exception.UserTooManyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

/** Controller for table pages */

@Controller
public class TableController {

    @GetMapping("/basic_table")
    public String basic_table(){

        return "table/basic_table"; // resources/templates/table/basic_table.html
    }

    // TODO : fix this (dynamic_table.html)
    @GetMapping("/dynamic_table")
    public String dynamic_table(Model model){

        // we'll parse table data dynamically
//        List<User> users = Arrays.asList(
//                new User("iori", "123"),
//                new User("may", "000"),
//                new User("ann", "123"),
//                new User("connie", "123") // if 503 error : TOO MUCH USERS -> remove one user here
//        );
//
//        model.addAttribute("users", users);

//        if (users.size() > 3){
//            throw new UserTooManyException();
//        }

        // TODO : implement below
        // get uses from DB

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
