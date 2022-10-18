package com.yen.resourceServer.controller;

// book p.3-52
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver/src/main/java/com/wudimanong/resourceserver/controller/UserResourcesController.java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserResourcesController {

    @Autowired
    UserResourceService userResourceService;
}
