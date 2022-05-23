package com.yen.springBootPOC3.controller;

import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.yen.springBootPOC3.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/** book p.85 */

@Controller
@RequestMapping(path="/demo")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/add")
    @RequestBody
    public String addNewUser
}
