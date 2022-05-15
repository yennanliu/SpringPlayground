package com.yen.springBootPOC2AdminSystem.controller;

// https://www.youtube.com/watch?v=TN2OO1vAoWs&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=51

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/** Controller For file upload page
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
     */
    @PostMapping("/upload")
    public String upload(
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestPart("headerImg") MultipartFile headerImg,
            @RequestPart("photos") MultipartFile[] photos
            ) throws IOException {

        log.info(">>> Upload info : email={}, username={}, headerImg size ={}, photo count={}", email, username, headerImg.getSize(), photos.length);

        // get current dir
        String outputDir = System.getProperty("user.dir") + "/output/";

        /** save uploaded single file (headerImg) (to server) */
        if (!headerImg.isEmpty()){
            String originalFilename = headerImg.getOriginalFilename();
            headerImg.transferTo(new File(outputDir + originalFilename));
        }

        /** save uploaded multiple files (photos) (to server) */
        if (photos.length > 0){
            for (MultipartFile photo : photos){
                if (!photo.isEmpty()){
                    String originalFilename = photo.getOriginalFilename();
                    photo.transferTo(new File(outputDir + originalFilename));
                }
            }
        }

        return "main";
    }

}
