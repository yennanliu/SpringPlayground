package com.example.demo.controller;

import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: PostController
 * Author:   longzhonghua
 * Date:     4/8/2019 10:20 AM
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@RestController
public class GetController {
    @Autowired
    RestTemplateBuilder restTemplateBuilder;
    @RequestMapping("/nparameters")
    //傳回String,不帶參數
    public String nparameters() {
        RestTemplate client= restTemplateBuilder.build();
        ResponseEntity<String> responseEntity = client.getForEntity("http://localhost:8080/getuser1", String.class);
        return responseEntity.getBody();
    }

    @RequestMapping("/withparameters1")
    //傳回String,帶參數
    public String withparameters1() {
        RestTemplate client= restTemplateBuilder.build();
        ResponseEntity<String> responseEntity = client.getForEntity("http://localhost:8080/getparameter?name={1}&id={2}", String.class, "hua",2);
        return responseEntity.getBody();
    }
    @RequestMapping("/withparameters2")
    //傳回String,帶參數
    public String withparameters2() {
        RestTemplate client= restTemplateBuilder.build();
        Map<String, String> map = new HashMap<>();
        map.put("name", "zhonghuaLong");

        ResponseEntity<String> responseEntity = client.getForEntity("http://localhost:8080/getparameter?name={name}&id=3", String.class, map);
        return responseEntity.getBody();
    }

    @RequestMapping("/user1")
    //傳回一個自訂型態的物件
    public User restUser1() {
        RestTemplate client= restTemplateBuilder.build();
        ResponseEntity<User> responseEntity = client.getForEntity("http://localhost:8080/getuser1", User.class);
        return responseEntity.getBody();
    }

    @RequestMapping("/getforobject")
    public User  getForObject() {
        RestTemplate client= restTemplateBuilder.build();
        User user = client.getForObject("http://localhost:8080/getuser1", User.class);
        return user;
}

}
