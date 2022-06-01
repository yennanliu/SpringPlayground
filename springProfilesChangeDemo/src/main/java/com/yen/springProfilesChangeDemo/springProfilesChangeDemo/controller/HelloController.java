package com.yen.springProfilesChangeDemo.springProfilesChangeDemo.controller;

// https://www.youtube.com/watch?v=newMNCS4sik&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=82
// https://www.youtube.com/watch?v=Eic1OfY_1ZY&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=83

import com.yen.springProfilesChangeDemo.springProfilesChangeDemo.bean.Person;
import com.yen.springProfilesChangeDemo.springProfilesChangeDemo.bean.Person2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    /**
     *  1) get person.name val from application.properties
     *  2) ${person.name:Joe} means : if can't get person.name, use "Joe" as default
     */
    @Value("${person.name:Joe}")

    private String name;

    @Value("${JAVA_HOME}")
    private String msg;

    @GetMapping("/")
    public String hello(){
        return "HELLO " + name;
    }

    @Autowired
    private Person person;

    @GetMapping("/test")
    public Person person(){
        return person;
    }

/**
 *   TODO : fix below:
 *
 *   Field person2 in com.yen.springProfilesChangeDemo.springProfilesChangeDemo.controller.HelloController required a bean of type 'com.yen.springProfilesChangeDemo.springProfilesChangeDemo.bean.Person2' that could not be found.
 *
 *    The injection point has the following annotations:
 * 	- @org.springframework.beans.factory.annotation.Autowired(required=true)
 *
 * Action:
 *
 * Consider defining a bean of type 'com.yen.springProfilesChangeDemo.springProfilesChangeDemo.bean.Person2' in your configuration.
 *
 * Process finished with exit code 1
 */
//    @Autowired
//    private Person2 person2;

//    @GetMapping("/test2")
//    public String person2(){
//
//        return person2.getClass().toString();
//    }

    @GetMapping("/java_home")
    public String getMsg(){
        //String msg = "/apache-maven-3.6.3/bin/mvn0";
        return msg;
    }

}