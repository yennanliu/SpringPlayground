package com.yen.controller;

// https://blog.csdn.net/weixin_43944305/article/details/115867077

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.stream.Stream;

@RestController
public class ResourceLoadController {

    @Value("classpath:test1.txt")
    private String inputResource;

    //@Value("${application.yml}")
    @Value("classpath:application.yml")
    private String configInfo;

    @GetMapping("/resource/test1")
    public void get() throws IOException {

        System.out.println(">>> inputResource = " + inputResource);
        System.out.println(">>> configInfo = " + configInfo);

        File file = ResourceUtils.getFile("classpath:test1.txt");
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String data = null;
        while((data = br.readLine()) != null) {
            System.out.println(data);
        }
        br.close();
        isr.close();
        fis.close();
    }

}
