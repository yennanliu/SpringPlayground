package com.yen.springBootPOC2AdminSystem.controller;

// https://www.youtube.com/watch?v=59dkU-lunaA&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=46
// https://www.youtube.com/watch?v=PpheT7laE_8&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=47
// https://www.youtube.com/watch?v=TOwcNVQtniU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=56
// https://www.youtube.com/watch?v=njvVPhCFH6o&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=66
// https://www.youtube.com/watch?v=pzL68_zvqK4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=67
// https://www.youtube.com/watch?v=pRexnC6ldW8&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=69

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springBootPOC2AdminSystem.bean.User;
import com.yen.springBootPOC2AdminSystem.bean.User2;
import com.yen.springBootPOC2AdminSystem.exception.UserTooManyException;
import com.yen.springBootPOC2AdminSystem.service.User2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.RedirectAttributesMethodArgumentResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

/** Controller for table pages */

@Controller
public class TableController {

    @Autowired
    User2Service user2Service;

    @GetMapping("/basic_table")
    public String basic_table(){

        return "table/basic_table"; // resources/templates/table/basic_table.html
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id,
                             @RequestParam(value="pn", defaultValue = "1") Integer pn,
                             RedirectAttributes ra){
        user2Service.removeById(id);
        ra.addAttribute("pn", pn);
        return "redirect:/dynamic_table";
    }

    // TODO : fix this (dynamic_table.html)
    @GetMapping("/dynamic_table")
    // pn : page value, we get it via request, also set its defaultValue
    public String dynamic_table(@RequestParam(value="pn", defaultValue = "1") Integer pn, Model model){

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

        // get uses from DB
        List<User2> list = user2Service.list();
        //model.addAttribute("users", list);

        // show page by page
        Page<User2> userPage = new Page<User2>(pn, 2);
        // page check result
        Page<User2> page = user2Service.page(userPage, null);

        model.addAttribute("page", page);

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
