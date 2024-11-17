package com.yen.springBootPOC3.controller;

import com.yen.springBootPOC3.entity.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/** book p.55 */

@Controller
public class BJController {

    @RequestMapping("/")
    public String index(Model model){
        List<Person> people = new ArrayList<Person>();

        Person single  = new Person("may", 11, "tokyo");
        Person p1 = new Person("joe", 3, "CA");
        Person p2 = new Person("mary", 30, "NJ");
        Person p3 = new Person("jack", 18, "NK");

        people.add(p1);
        people.add(p2);
        people.add(p3);

        model.addAttribute("singlePerson", single);
        model.addAttribute("people", people);
        return "index";
    }

}
