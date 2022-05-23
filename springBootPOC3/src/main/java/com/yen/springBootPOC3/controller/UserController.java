package com.yen.springBootPOC3.controller;

import com.yen.springBootPOC3.dao.UserCrudRepository;
import com.yen.springBootPOC3.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** book p.85 */

@Controller
@RequestMapping(path="/demo")
public class UserController {

    @Autowired
    private UserCrudRepository userCrudRepository;

    @GetMapping("/add")
    @ResponseBody
    public String addNewUser(@RequestParam String firstname, @RequestParam String lastname){
        User user = new User(firstname, lastname);
        userCrudRepository.save(user);
        return "saved";
    }

    @GetMapping("/finduser/{lastname}")
    @ResponseBody
    public String findUser(@RequestParam("lastname") String lastname){
        List<User> userList = userCrudRepository.findByLastName(lastname);
        String users = "";
        for (User user:userList){
            users += user.toString() + "  ";
        }
        return users;
    }

}
