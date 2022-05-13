package com.yen.springBootPOC2AdminSystem.controller;

// https://www.youtube.com/watch?v=TN2OO1vAoWs&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=51

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/** For file upload
 *
 *   1) http://localhost:8888/form_layouts
 */

@Slf4j
@Controller
public class FormController {

    @GetMapping("/form_layouts")
    public String form_layouts(){
        return  "form/form_layouts";
    }

    /**
     *  1) MultipartFile will encapsulate upload files automatically
     *  2)
     */
    @PostMapping("/upload")
    public String upload(
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestPart("headerImg") MultipartFile headerImg,
            @RequestPart("photos") MultipartFile[] photos
            ){

        log.info(">>> Upload info : email={}, username={}, headerImg size ={}, photo count={}", email, username, headerImg.getSize(), photos.length);

        return "main";
    }

}
